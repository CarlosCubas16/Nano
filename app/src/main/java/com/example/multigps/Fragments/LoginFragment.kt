package com.example.multigps.Fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.*
import com.android.volley.toolbox.*
import com.example.multigps.*
import com.example.multigps.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap
import com.example.multigps.R

class LoginFragment(): Fragment() {
    private var imLeft: ImageButton? = null
    private var layoutEmail: TextInputLayout? = null
    private var txtEmail: TextInputEditText? = null
    private var layoutPassword: TextInputLayout? = null
    private var txtPassword: TextInputEditText? = null
    private var btnSignIng: Button? = null
    private var txtForgotPassword: TextView? = null

    private var dialog: ProgressDialog? = null

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        root = binding.root
        init()
        return root
    }

    private fun init() {
        imLeft = root?.findViewById(R.id.im_auth)
        layoutEmail = root?.findViewById(R.id.layoutEmail)
        txtEmail = root?.findViewById(R.id.txtEmail)
        layoutPassword = root?.findViewById(R.id.layoutPassword)
        txtPassword = root?.findViewById(R.id.txtPassword)
        btnSignIng = root?.findViewById(R.id.btn_signIn)
        txtForgotPassword = root?.findViewById(R.id.txt_forgotPassword)
        dialog = ProgressDialog(context)
        dialog!!.setCancelable(false)

        imLeft!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_loginFragment_to_authFragment)
        })

        txtForgotPassword!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        })

        txtEmail!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!txtEmail?.getText().toString().isEmpty()) {
                    layoutEmail!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        txtPassword!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (txtPassword!!.getText().toString().length > 7) {
                    layoutPassword!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        btnSignIng!!.setOnClickListener {
            if(validate())
            {
                login()
            }
        }

        /*txtEmail!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
            {
                val llp = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                llp.setMargins(0, 0, 0, 0) // llp.setMargins(left, top, right, bottom);
                linearLogin!!.layoutParams = llp
            }
        }*/

        /*txtPassword!!.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if(validate())
                {
                    login()
                }
                true
            } else {
                false
            }
        }*/
    }

    private fun validate(): Boolean {
        if (txtEmail!!.text.toString().isEmpty()) {
            layoutEmail!!.isErrorEnabled = true
            layoutEmail!!.error = "Email es requerido"
            return false
        }



        if (txtPassword!!.text.toString().length < 8) {
            layoutPassword!!.isErrorEnabled = true
            layoutPassword!!.error = "Requiere al menos 8 caracteres"
            return false
        }
        return true
    }

    private fun login() {
        dialog!!.setMessage("Iniciando sesión")
        dialog!!.show()

        val request: StringRequest = object : StringRequest(
            Method.POST,
            Constant.LOGIN,
            Response.Listener { response: String? ->
                try {
                    val `object` = JSONObject(response)
                    Log.i("Response", `object`.toString())
                    val message = `object`.getString("message")
                    val respuesta: Boolean = `object`.getBoolean("success")
                    if (respuesta) {
                        val user = `object`.getJSONObject("data")
                        val userPref =
                            context?.getSharedPreferences("user",
                                AppCompatActivity.MODE_PRIVATE
                            )
                        val editor = userPref?.edit()
                        editor!!.putString("token", user.getString("token"))
                        editor!!.putString("name", user.getString("name"))
                        editor!!.putString("email", user.getString("email"))
                        //editor!!.putString("photo", user.getString("photo"))

                        editor.apply()

                        dialog!!.dismiss()

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        showHome(user.getString("email"),ProviderType.BASIC)
                    }
                    else
                    {
                        dialog!!.dismiss()
                        showAlert("Error", message)
                    }
                } catch (e: JSONException) {
                    dialog!!.dismiss()
                    showAlert("Error", "Se ha producido un error al autenticar el usuario")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                dialog!!.dismiss()
                showAlert("Error", "Se ha producido un error al autenticar el usuario")
                error.printStackTrace() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["email"] = txtEmail!!.text.toString()
                map["password"] = txtPassword!!.text.toString()
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

    private fun showHome(email: String, provider: ProviderType)
    {
        val homeIntent = Intent(context as AuthActivity?, HomeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
        (context as AuthActivity?)!!.finish()
    }
}