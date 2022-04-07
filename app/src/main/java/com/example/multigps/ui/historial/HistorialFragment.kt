package com.example.multigps.ui.historial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.multigps.R
import com.example.multigps.databinding.FragmentHistorialBinding

class HistorialFragment : Fragment() {
    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)
        root = binding.root
        init()
        return root
    }

    fun init() {

    }

}