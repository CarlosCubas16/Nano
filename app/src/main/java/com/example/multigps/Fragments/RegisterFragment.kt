package com.example.multigps.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
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
import com.example.multigps.R
import com.example.multigps.databinding.FragmentRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class RegisterFragment: Fragment() {
    private var imLeft: ImageButton? = null
    private var layoutDocumento: TextInputLayout? = null
    private var txtDocumento: TextInputEditText? = null
    private var layoutCelular: TextInputLayout? = null
    private var txtCelular: TextInputEditText? = null
    private var layoutEmail: TextInputLayout? = null
    private var txtEmail: TextInputEditText? = null
    private var layoutPassword: TextInputLayout? = null
    private var txtPassword: TextInputEditText? = null
    private var layoutConfirmPassword: TextInputLayout? = null
    private var txtConfirmPassword: TextInputEditText? = null
    private var btnSignUp: Button? = null
    private var txtSignIn: TextView? = null

    private var dialog:ProgressDialog? = null

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        root = binding.root
        init()
        return root
    }

    private fun init() {
        imLeft = root?.findViewById(R.id.im_auth)
        layoutDocumento = root?.findViewById(R.id.layoutDocumento)
        txtDocumento = root?.findViewById(R.id.txtDocumento)
        layoutCelular = root?.findViewById(R.id.layoutCelular)
        txtCelular = root?.findViewById(R.id.txtCelular)
        layoutEmail = root?.findViewById(R.id.layoutEmail)
        txtEmail = root?.findViewById(R.id.txtEmail)
        layoutPassword = root?.findViewById(R.id.layoutPassword)
        txtPassword = root?.findViewById(R.id.txtPassword)
        layoutConfirmPassword = root?.findViewById(R.id.layoutConfirmPassword)
        txtConfirmPassword = root?.findViewById(R.id.txtConfirmPassword)
        btnSignUp = root?.findViewById(R.id.btn_signUp)
        txtSignIn = root?.findViewById(R.id.txtSignIn)

        dialog = ProgressDialog(context)
        dialog!!.setCancelable(false)

        imLeft!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_registerFragment_to_authFragment)
        })

        txtSignIn!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_registerFragment_to_loginFragment)
        })

        txtDocumento!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (txtDocumento?.getText().toString().length == 8) {
                    layoutDocumento!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        txtCelular!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!txtCelular?.getText().toString().isEmpty()) {
                    layoutCelular!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
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

        txtConfirmPassword!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (txtConfirmPassword!!.getText().toString() == txtPassword!!.getText().toString()) {
                    layoutConfirmPassword!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        btnSignUp!!.setOnClickListener {
            if(validate())
            {
                register()
            }
        }
    }

    private fun validate(): Boolean {
        if (txtDocumento!!.text.toString().length != 8) {
            layoutDocumento!!.isErrorEnabled = true
            layoutDocumento!!.error = "Documento debe tener 8 caracteres"
            return false
        }

        if (txtCelular!!.text.toString().isEmpty()) {
            layoutCelular!!.isErrorEnabled = true
            layoutCelular!!.error = "Celular es requerido"
            return false
        }

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
        if (!txtConfirmPassword!!.text.toString().equals(txtPassword!!.text.toString())) {
            layoutConfirmPassword!!.isErrorEnabled = true
            layoutConfirmPassword!!.error = "Contraseñas diferentes"
            return false
        }
        return true
    }

    private fun register() {
        dialog!!.setMessage("Registrando...")
        dialog!!.show()

        val request: StringRequest = object : StringRequest(
            Method.POST,
            Constant.REGISTER,
            Response.Listener { response: String? ->
                try {
                    val `object` = JSONObject(response)
                    val message = `object`.getString("message")
                    val respuesta: Boolean = `object`.getBoolean("success")
                    Log.i("Response", `object`.toString())
                    if (respuesta) {
                        dialog!!.dismiss()

                        showAlert("Éxito","Para iniciar sesión debes confirmar tu correo electronico")

                        Navigation.findNavController(root).navigate(R.id.action_registerFragment_to_loginFragment)
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
                showAlert("Error","Se ha producido un error al registrar al usuario")
                error.printStackTrace() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["documento"] = txtDocumento!!.text.toString()
                map["telefono_movil"] = txtCelular!!.text.toString()
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
}