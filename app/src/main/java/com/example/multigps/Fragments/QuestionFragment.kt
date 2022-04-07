package com.example.multigps.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.multigps.R
import com.example.multigps.databinding.FragmentQuestionBinding
import pl.droidsonroids.gif.GifImageView

class QuestionFragment: Fragment() {
    private var txtQuestion:TextView? = null
    private var imageConatiner: GifImageView? = null
    private var btnNo:Button? = null
    private var btnYes:Button? = null

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        root = binding.root
        init()
        return root
    }

    private fun init()
    {
        btnYes = root!!.findViewById(R.id.btn_yes)
        btnYes!!.setOnClickListener(View.OnClickListener { v: View? ->
            Navigation.findNavController(root).navigate(R.id.action_questionFragment_to_authFragment)
        })
    }
}