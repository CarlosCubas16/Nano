package com.example.multigps.ui.perfiles

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.multigps.Adapters.perfiles.PerfilAdapter
import com.example.multigps.HomeActivity
import com.example.multigps.MyToolbarPrimary
import com.example.multigps.R
import com.example.multigps.databinding.FragmentPerfilesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PerfilesFragment:Fragment() {
    private lateinit var perfilesViewModel: PerfilesViewModel
    private lateinit var perfilesViewModelFactory: PerfilesViewModelFactory
    private var _binding: FragmentPerfilesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerPerfiles: RecyclerView
    private lateinit var swipePerfiles: SwipeRefreshLayout
    private lateinit var root: View
    private lateinit var floaction_agregar: FloatingActionButton
    private lateinit var perfilesAdapter: PerfilAdapter

    private lateinit var token: String

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilesBinding.inflate(inflater, container, false)
        root = binding.root

        MyToolbarPrimary().show(activity as HomeActivity,root,"Perfiles", false,true)

        val userPref = root.context.getSharedPreferences("user",  AppCompatActivity.MODE_PRIVATE)
        token = userPref.getString("token", null).toString()

        recyclerPerfiles = binding.recyclerPerfiles
        swipePerfiles = binding.swipePerfiles

        perfilesViewModelFactory = PerfilesViewModelFactory(root.context,token,swipePerfiles)
        perfilesViewModel = ViewModelProvider(this,perfilesViewModelFactory)[PerfilesViewModel::class.java]
        root = binding.root

        recyclerPerfiles!!.setHasFixedSize(true)
        recyclerPerfiles!!.layoutManager = GridLayoutManager(root.context, 2)

        perfilesViewModel.elementos.observe(viewLifecycleOwner) {
            perfilesAdapter = PerfilAdapter(root.context, it)
            recyclerPerfiles.adapter = perfilesAdapter
        }

        swipePerfiles.setOnRefreshListener {
            perfilesViewModel.loadPerfiles()
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.agregar -> {
                Navigation.findNavController(root).navigate(R.id.action_perfilesFragment_to_perfilesCUFragment)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}