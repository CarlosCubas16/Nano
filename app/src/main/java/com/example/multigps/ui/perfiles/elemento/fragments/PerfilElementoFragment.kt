package com.example.multigps.ui.perfiles.elemento.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.multigps.R
import com.example.multigps.databinding.FragmentPerfilElementoBinding
import com.example.multigps.ui.perfiles.elemento.PerfilViewModel
import com.example.multigps.ui.perfiles.elemento.PerfilesElementoFragment

class PerfilElementoFragment: Fragment() {

    private var _binding: FragmentPerfilElementoBinding? = null
    private val binding get() = _binding!!

    val perfilViewModel: PerfilViewModel by viewModels({ requireParentFragment() })

    private lateinit var tvElementoSobre: TextView
    private lateinit var tvElementoEditar: TextView
    private lateinit var tvElementoEdad: TextView
    private lateinit var tvElementoTalla: TextView
    private lateinit var tvElementoFechaNacimiento: TextView
    private lateinit var tvElementoPeso: TextView
    private lateinit var tvElementoCaracteristicas: TextView
    private lateinit var tvElementoDescripcion: TextView

    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilElementoBinding.inflate(inflater,container,false)
        root  = binding.root

        perfilViewModel.pagina.value = "ELEMENTO"

        tvElementoSobre = binding.tvElementoSobre
        tvElementoEditar = binding.tvElementoEditar
        tvElementoEdad = binding.tvElementoEdad
        tvElementoTalla = binding.tvElementoTalla
        tvElementoFechaNacimiento = binding.tvElementoFechaNacimiento
        tvElementoPeso = binding.tvElementoPeso
        tvElementoCaracteristicas = binding.tvElementoCaracteristicas
        tvElementoDescripcion = binding.tvElementoDescripcion

        perfilViewModel.elemento.observe(viewLifecycleOwner, Observer {
            Log.i("elemento", it.toString())
            tvElementoSobre.text = "Sobre " + it.nombre!!.split(" ")[0]
            tvElementoEdad.text = "Edad: " + it!!.edad
            tvElementoTalla.text = "Altura: " + it!!.altura
            tvElementoFechaNacimiento.text = "F. Nacimiento: " + it!!.fecha_nacimiento
            tvElementoPeso.text = "Peso: " + it!!.peso
            tvElementoDescripcion.text = it!!.descripcion
        })

        tvElementoEditar.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("elemento", perfilViewModel.elemento.value)
            Navigation.findNavController(it).navigate(R.id.action_perfilesElementoFragment_to_perfilesCUFragment, bundle)
        }

        return root
    }
}