package com.example.multigps.Fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.multigps.AuthActivity
import com.example.multigps.Constant
import com.example.multigps.ProviderType
import com.example.multigps.R
import com.example.multigps.databinding.FragmentForgotPasswordBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class ForgotPasswordFragment: Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    private var imLeft: ImageButton? = null
    private var layoutEmail: TextInputLayout? = null
    private var txtEmail: TextInputEditText? = null
    private var btnForgot: Button? = null
    private var txtRegister: TextView? = null

    private var dialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        root = binding.root
        init()
        return root
    }

    fun init()
    {
        imLeft = root?.findViewById(R.id.im_auth)
        layoutEmail = root?.findViewById(R.id.layoutEmail)
        txtEmail = root?.findViewById(R.id.txtEmail)
        btnForgot = root?.findViewById(R.id.btn_Forgot)
        txtRegister = root?.findViewById(R.id.txt_register)

        dialog = ProgressDialog(context)
        dialog!!.setCancelable(false)

        imLeft!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        })

        txtRegister!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_forgotPasswordFragment_to_registerFragment)
        })

        btnForgot!!.setOnClickListener {
            if(validate())
            {
                restore()
            }
        }
    }

    fun validate(): Boolean {
        if (txtEmail!!.text.toString().isEmpty()) {
            layoutEmail!!.isErrorEnabled = true
            layoutEmail!!.error = "Email es requerido"
            return false
        }
        return true
    }

    private fun restore() {
        dialog!!.setMessage("Verificando email")
        dialog!!.show()

        val request: StringRequest = object : StringRequest(
            Method.POST,
            Constant.RESTORE_PASSWORD,
            Response.Listener { response: String? ->
                try {
                    val `object` = JSONObject(response)
                    Log.i("Response", `object`.toString())
                    val respuesta: Boolean = `object`.getBoolean("success")
                    if (respuesta) {
                        dialog!!.dismiss()

                        showAlert("Éxito","Se te envió un correo electrónico para restablecer contraseña")

                        Navigation.findNavController(root).navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                    }
                    else
                    {
                        val message = `object`.getString("message")
                        dialog!!.dismiss()
                        showAlert("Error", message)
                    }
                } catch (e: JSONException) {
                    showAlert("Error", "Se ha producido un error al restablecer contraseña")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                dialog!!.dismiss()
                showAlert("Error", "Se ha producido un error al restablecer contraseña")
                error.printStackTrace() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["email"] = txtEmail!!.text.toString()
                return map
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private fun showAlert(title: String, message: String)
    {
        val builder = AlertDialog.Builder(context as AuthActivity?)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}