package com.example.multigps.ui.perfiles.elemento.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.multigps.R
import com.example.multigps.databinding.FragmentPerfilElementoBinding
import com.example.multigps.databinding.FragmentPerfilLocalizadorBinding
import com.example.multigps.ui.perfiles.elemento.PerfilViewModel

class PerfilLocalizadorFragment: Fragment() {
    private var _binding: FragmentPerfilLocalizadorBinding? = null
    private val binding get() = _binding!!

    val perfilViewModel: PerfilViewModel by viewModels({ requireParentFragment() })

    private lateinit var root: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilLocalizadorBinding.inflate(inflater,container,false)
        root  = binding.root
        perfilViewModel.pagina.value = "LOCALIZADOR"
        return root
    }
}