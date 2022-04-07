package com.example.multigps.ui.perfiles.cu

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.multigps.*
import com.example.multigps.Models.Elemento
import com.example.multigps.Models.TipoElemento
import com.example.multigps.databinding.FragmentPerfilesCuBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PerfilesCuFragment: Fragment() {
    private lateinit var perfilesCuViewModel: PerfilesCuViewModel
    private lateinit var perfilesCuViewModelFactory: PerfilesCuViewModelFactory

    private var _binding: FragmentPerfilesCuBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    private var token: String? = null

    private var elemento: Elemento? = null

    private lateinit var civPerfil: CircleImageView
    private lateinit var tvSelectPerfil: TextView
    private lateinit var layoutTipoElemento: TextInputLayout
    private lateinit var autocompleteTipoElemento: AutoCompleteTextView
    private lateinit var layoutNombre: TextInputLayout
    private lateinit var txtNombre: TextInputEditText
    private lateinit var layoutDescripcion: TextInputLayout
    private lateinit var txtDescripcion: TextInputEditText
    private lateinit var layoutPeso: TextInputLayout
    private lateinit var txtPeso: TextInputEditText
    private lateinit var layoutEdad: TextInputLayout
    private lateinit var txtEdad: TextInputEditText
    private lateinit var layoutFechaNacimiento: TextInputLayout
    private lateinit var txtFechaNacimiento: TextInputEditText
    private lateinit var layoutTalla: TextInputLayout
    private lateinit var txtTalla: TextInputEditText
    private var imageUri: Uri? = null
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilesCuBinding.inflate(inflater, container, false)
        root = binding.root

        civPerfil = binding.civPerfil
        tvSelectPerfil = binding.tvSelectPerfil
        layoutTipoElemento = binding.layoutTipoElemento
        autocompleteTipoElemento = binding.autocompleteTipoElemento
        layoutNombre = binding.layoutNombre
        txtNombre = binding.txtNombre
        layoutDescripcion = binding.layoutDescripcion
        txtDescripcion = binding.txtDescripcion
        layoutPeso = binding.layoutPeso
        txtPeso = binding.txtPeso
        layoutEdad = binding.layoutEdad
        txtEdad = binding.txtEdad
        layoutFechaNacimiento = binding.layoutFechaNacimiento
        txtFechaNacimiento = binding.txtFechaNacimiento
        layoutTalla = binding.layoutTalla
        txtTalla = binding.txtTalla

        dialog = ProgressDialog(context)
        dialog!!.setCancelable(false)

        val userPref = root.context.getSharedPreferences("user",  AppCompatActivity.MODE_PRIVATE)
        val name = userPref.getString("name", null)
        val token_ = userPref.getString("token", null)
        token = token_

        perfilesCuViewModelFactory = PerfilesCuViewModelFactory(root.context,token!!)
        perfilesCuViewModel = ViewModelProvider(this,perfilesCuViewModelFactory)[PerfilesCuViewModel::class.java]

        var elemento_ = arguments?.get("elemento")
        if(elemento_ != null)
        {
            elemento = elemento_ as Elemento
            MyToolbarPrimary().show(activity as HomeActivity,root,"Editar perfil", true, true)
            autocompleteTipoElemento!!.setText(elemento!!.tipo_elemento!!.nombre,false)
            txtNombre!!.setText(elemento!!.nombre.toString())
            txtDescripcion!!.setText(elemento!!.descripcion.toString())
            txtPeso!!.setText(elemento!!.peso.toString())
            txtEdad!!.setText(elemento!!.edad.toString())
            txtFechaNacimiento!!.setText(elemento!!.fecha_nacimiento.toString())
            txtTalla!!.setText(elemento!!.altura.toString())
            perfilesCuViewModel.tipoFormulario.value = "EDIT"
        }
        else
        {
            MyToolbarPrimary().show(activity as HomeActivity,root,"Crear perfil", true, true)
            perfilesCuViewModel.tipoFormulario.value = "CREATE"
        }

        init()

        return root
    }

    fun init()
    {
        perfilesCuViewModel.tipos_elemento_string.observe(viewLifecycleOwner, Observer {
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, it)
            (layoutTipoElemento.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        })

        tvSelectPerfil.setOnClickListener{
            val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            cameraIntent.type = "image/*"
            startActivityForResult(cameraIntent, 100)
        }

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateLable(myCalendar)
        }

        txtFechaNacimiento.setOnClickListener {
            DatePickerDialog(root.context, datePicker, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        autocompleteTipoElemento!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!autocompleteTipoElemento?.getText().toString().isEmpty()) {
                    layoutTipoElemento!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        txtNombre!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!txtNombre?.getText().toString().isEmpty()) {
                    layoutNombre!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        txtDescripcion!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!txtDescripcion?.getText().toString().isEmpty()) {
                    layoutDescripcion!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        txtPeso!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!txtPeso?.getText().toString().isEmpty()) {
                    layoutPeso!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        txtEdad!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!txtEdad?.getText().toString().isEmpty()) {
                    layoutEdad!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        txtFechaNacimiento!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!txtFechaNacimiento?.getText().toString().isEmpty()) {
                    layoutFechaNacimiento!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        txtTalla!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!txtTalla?.getText().toString().isEmpty()) {
                    layoutTalla!!.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun validate(): Boolean {
        var correcto: Boolean = true
        if(autocompleteTipoElemento!!.text.toString().isEmpty())
        {
            layoutTipoElemento.isErrorEnabled = true
            layoutTipoElemento.error = "El tipo de elemento es requerido"
            correcto = false
        }

        if(txtNombre!!.text.toString().isEmpty())
        {
            layoutNombre.isErrorEnabled = true
            layoutNombre.error = "El nombre es requerido"
            correcto = false
        }

        if(txtDescripcion!!.text.toString().isEmpty())
        {
            layoutDescripcion.isErrorEnabled = true
            layoutDescripcion.error = "La descripcion es requerida"
            correcto = false
        }

        if(txtPeso!!.text.toString().isEmpty())
        {
            layoutPeso.isErrorEnabled = true
            layoutPeso.error = "El peso es requerido"
            correcto = false
        }

        if(txtEdad!!.text.toString().isEmpty())
        {
            layoutEdad.isErrorEnabled = true
            layoutEdad.error = "La edad es requerida"
            correcto = false
        }

        if(txtFechaNacimiento!!.text.toString().isEmpty())
        {
            layoutFechaNacimiento.isErrorEnabled = true
            layoutFechaNacimiento.error = "La fecha es requerida"
            correcto = false
        }

        if(txtTalla!!.text.toString().isEmpty())
        {
            layoutTalla.isErrorEnabled = true
            layoutTalla.error = "La talla es requerida"
            correcto = false
        }

        return correcto
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

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        var text_fecha = txtFechaNacimiento as TextView
        text_fecha.text = sdf.format(myCalendar.time)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_check, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if(perfilesCuViewModel.tipoFormulario.value == "EDIT")
                {
                    val bundle = Bundle()
                    bundle.putSerializable("elemento",elemento!!)
                    Navigation.findNavController(root).navigate(R.id.action_perfilesCUFragment_to_perfilesElementoFragment, bundle)
                }
                else
                {
                    Navigation.findNavController(root).navigate(R.id.action_perfilesCUFragment_to_perfilesFragment)
                }
            }
            R.id.guardar -> {
                if (validate())
                {
                    if(perfilesCuViewModel.tipoFormulario.value == "EDIT")
                    {
                        edit()
                    }
                    else
                    {
                        save()
                    }
                }
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                civPerfil?.setImageURI(data?.getData())
                imageUri = data?.getData()
            }
        }
    }

    private fun save()
    {
        dialog!!.setMessage("Registrando elemento")
        dialog!!.show()

        val request: StringRequest = object : StringRequest(
            Method.POST,
            Constant.ELEMENTOS,
            Response.Listener { response: String? ->
                try {
                    val `object` = JSONObject(response)
                    Log.i("Response", `object`.toString())
                    val message = `object`.getString("message")
                    val respuesta: Boolean = `object`.getBoolean("success")
                    if (respuesta) {
                        dialog!!.dismiss()
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(root).navigate(R.id.action_perfilesCUFragment_to_perfilesFragment)
                    }
                    else
                    {
                        dialog!!.dismiss()
                        showAlert("Error", message)
                    }
                } catch (e: JSONException) {
                    dialog!!.dismiss()
                    showAlert("Error", "Se ha producido un error al registrar el elemento.")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                dialog!!.dismiss()
                showAlert("Error", "Se ha producido un error al registrar el elemento.")
                error.printStackTrace() }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["Authorization"] = "Bearer " + token
                return map
            }
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                val tipo = perfilesCuViewModel.tipos_elemento.value?.find { it.nombre == autocompleteTipoElemento!!.text.toString() }

                map["tipo_elemento_id"] = tipo!!.id
                map["nombre"] = txtNombre!!.text.toString()
                map["descripcion"] = txtDescripcion!!.text.toString()
                map["peso"] = txtPeso!!.text.toString()
                map["edad"] = txtEdad!!.text.toString()
                map["fecha_nacimiento"] = txtFechaNacimiento!!.text.toString()
                map["altura"] = txtTalla!!.text.toString()
                return map
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private fun edit()
    {
        dialog!!.setMessage("Editando elemento")
        dialog!!.show()

        val tipo = perfilesCuViewModel.tipos_elemento.value?.find { it.nombre == autocompleteTipoElemento!!.text.toString() }
        val request: StringRequest = object : StringRequest(
            Method.PUT,
            Constant.ELEMENTOS + "/" + elemento!!.id,
            Response.Listener { response: String? ->
                try {
                    val `object` = JSONObject(response)
                    Log.i("Response", `object`.toString())
                    val message = `object`.getString("message")
                    val respuesta: Boolean = `object`.getBoolean("success")
                    if (respuesta) {
                        var objeto = `object`.getJSONObject("data")
                        val elem = Elemento(
                            objeto.getInt("id").toString(),
                            objeto.getString("tipo_elemento_id"),
                            objeto.getInt("cliente_id").toString(),
                            objeto.getString("nombre"),
                            objeto.getString("descripcion"),
                            objeto.getString("peso"),
                            objeto.getString("edad"),
                            objeto.getString("fecha_nacimiento"),
                            objeto.getString("altura"),
                            objeto.getString("estado"),
                            objeto.getString("url_imagen"),
                            objeto.getString("nombre_imagen"),
                            objeto.getString("estado_asignacion"),
                            tipo as TipoElemento
                        )
                        val bundle = Bundle()
                        bundle.putSerializable("elemento", elem)
                        Navigation.findNavController(root).navigate(R.id.action_perfilesCUFragment_to_perfilesElementoFragment,bundle)
                        dialog!!.dismiss()
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        dialog!!.dismiss()
                        showAlert("Error", message)
                    }
                } catch (e: JSONException) {
                    dialog!!.dismiss()
                    showAlert("Error", "Se ha producido un error al registrar el elemento.")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                dialog!!.dismiss()
                showAlert("Error", "Se ha producido un error al registrar el elemento.")
                error.printStackTrace() }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["Authorization"] = "Bearer " + token
                return map
            }
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["tipo_elemento_id"] = tipo!!.id
                map["nombre"] = txtNombre!!.text.toString()
                map["descripcion"] = txtDescripcion!!.text.toString()
                map["peso"] = txtPeso!!.text.toString()
                map["edad"] = txtEdad!!.text.toString()
                map["fecha_nacimiento"] = txtFechaNacimiento!!.text.toString()
                map["altura"] = txtTalla!!.text.toString()
                return map
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(request)
    }
}