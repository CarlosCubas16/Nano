package com.example.multigps.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.multigps.R
import com.example.multigps.databinding.FragmentAuthBinding

class AuthFragment: Fragment() {
    private var imLeft: ImageButton? = null
    private var btnSigIn: Button? = null
    private var btnSignuP: Button? = null

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        root = binding.root
        init()
        return root
    }

    private fun init()
    {
        imLeft = root?.findViewById(R.id.im_auth)
        btnSigIn = root?.findViewById(R.id.btn_signInAuth)
        btnSignuP = root?.findViewById(R.id.btn_signUpAuth)

        imLeft!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_authFragment_to_questionFragment)
        })

        btnSigIn!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_authFragment_to_loginFragment)
        })

        btnSignuP!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_authFragment_to_registerFragment)
        })
    }
}