package com.example.multigps.ui.cuenta

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.multigps.*
import com.example.multigps.databinding.FragmentCuentaBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class CuentaFragment : Fragment() {
    private var _binding: FragmentCuentaBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    private var dialog: ProgressDialog? = null
    private var txtMiInfoCuenta: TextView? = null
    private var txtSobreCuenta: TextView? = null
    private var txtCerrarSesion: TextView? = null

    var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        root = binding.root

        val userPref = root.context.getSharedPreferences("user",  AppCompatActivity.MODE_PRIVATE)
        val name = userPref.getString("name", null)
        token = userPref.getString("token", null)

        MyToolbarPrimary().show(activity as HomeActivity,root,name?: "", false, true)

        txtCerrarSesion = binding.txtCerrarSesionCuenta
        txtSobreCuenta = binding.txtSobreCuenta
        txtMiInfoCuenta = binding.txtMiInfoCuenta
        init()
        return root
    }

    fun init() {
        dialog = ProgressDialog(context)
        dialog!!.setCancelable(false)

        txtMiInfoCuenta!!.setOnClickListener{

        }

        txtSobreCuenta!!.setOnClickListener {
            val _link = Uri.parse(Constant.URL)
            val intent = Intent(Intent.ACTION_VIEW, _link)
            startActivity(intent)
        }

        txtCerrarSesion!!.setOnClickListener{
            logout()
        }

    }

    fun logout()
    {
        dialog!!.setMessage("Cerrando sesión")
        dialog!!.show()
        val request: StringRequest = object : StringRequest(
            Method.POST,
                Constant.LOGOUT,
            Response.Listener { response: String? ->
                try {
                    val obj = JSONObject(response)
                    val message = obj.getString("message")
                    val respuesta: Boolean = obj.getBoolean("success")
                    if (respuesta) {
                        dialog!!.dismiss()

                        val prefs = root.context.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                        prefs.remove("email").commit()
                        prefs.remove("provider").commit()
                        prefs.apply()

                        val userPrefs = root.context.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                        userPrefs.clear()
                        userPrefs.apply()

                        val i = Intent(context as HomeActivity?, AuthActivity::class.java)
                        startActivity(i)
                        (context as HomeActivity?)!!.finish()
                    }
                    else
                    {
                        dialog!!.dismiss()
                        showAlert("Error",message)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                dialog!!.dismiss()
                try{
                    if(error.networkResponse.statusCode == 401)
                    {
                        // showAlert("Error","No ha iniciado sesión o su token ha expirado.")
                        val prefs = root.context.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                        prefs.remove("email").commit()
                        prefs.remove("provider").commit()
                        prefs.apply()

                        val userPrefs = root.context.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                        userPrefs.clear()
                        userPrefs.apply()

                        val i = Intent(context as HomeActivity?, AuthActivity::class.java)
                        startActivity(i)
                        (context as HomeActivity?)!!.finish()
                    }
                    else
                    {
                        showAlert("Error","Ocurrió un error, vuelve a intentarlo.")
                    }
                }
                catch (e:Exception)
                {
                    showAlert("Error","Ocurrió un error, vuelve a intentarlo.")
                    val prefs = root.context.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                    prefs.remove("email").commit()
                    prefs.remove("provider").commit()
                    prefs.apply()

                    val userPrefs = root.context.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                    userPrefs.clear()
                    userPrefs.apply()

                    val i = Intent(context as HomeActivity?, AuthActivity::class.java)
                    startActivity(i)
                    (context as HomeActivity?)!!.finish()
                }
                error.printStackTrace()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["Content-Type"] = "application/json"
                map["X-Requested-With"] = "XMLHttpRequest"
                map["Authorization"] = "Bearer " + token
                return map
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private fun showAlert(title: String, message: String)
    {
        val builder = AlertDialog.Builder(context as HomeActivity?)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}