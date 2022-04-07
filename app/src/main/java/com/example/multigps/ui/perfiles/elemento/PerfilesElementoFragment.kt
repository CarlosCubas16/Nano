package com.example.multigps.ui.perfiles.elemento

import android.R.id
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.multigps.HomeActivity
import com.example.multigps.Models.Elemento
import com.example.multigps.R
import com.example.multigps.databinding.FragmentPerfilesElementoBinding
import com.example.multigps.ui.perfiles.elemento.fragments.PerfilElementoFragment
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView
import android.R.id.tabs
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.multigps.ui.perfiles.elemento.fragments.PerfilLocalizadorFragment
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import androidx.viewpager2.widget.ViewPager2
import com.example.multigps.ui.perfiles.elemento.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class PerfilesElementoFragment: Fragment() {
    private var _binding: FragmentPerfilesElementoBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    private val perfilViewModel by viewModels<PerfilViewModel>()

    private lateinit var toolbaPerfilElemento: Toolbar
    private lateinit var nameElementoPerfil: TextView
    private lateinit var nameElementoPerfilAux: TextView
    private lateinit var civElementoPerfil: CircleImageView
    private lateinit var civElementoPerfilAux: CircleImageView
    private lateinit var ivChangePerfil: ImageView
    private lateinit var tablayoutPerfilesElemento: TabLayout
    private lateinit var viewpagerPerfilesElemento: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private  lateinit var elemento: Elemento

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilesElementoBinding.inflate(inflater, container, false)
        root = binding.root

        ocultar()

        toolbaPerfilElemento = binding.toolbarPerfilElemento1
        tablayoutPerfilesElemento = binding.tablayoutPerfilesElemento
        viewpagerPerfilesElemento = binding.viewpagerPerfilesElemento

        nameElementoPerfil = binding.nameElementoPerfil
        nameElementoPerfilAux = binding.nameElementoPerfilAux
        civElementoPerfil = binding.civElementoPerfil
        civElementoPerfilAux = binding.civElementoPerfilAux

        elemento = arguments?.get("elemento") as Elemento
        perfilViewModel.elemento.value = elemento

        /*loadFragment(PerfilElementoFragment())

        tablayoutPerfilesElemento.setOnTabSelectedListener(
            object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when(tab.position)
                    {
                        0 -> {
                            loadFragment(PerfilElementoFragment())
                        }
                        1 -> {
                            loadFragment(PerfilLocalizadorFragment())
                        }
                        else -> {
                            loadFragment(PerfilElementoFragment())
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    // ...
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    // ...
                }
            }
        )*/

        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewpagerPerfilesElemento.adapter = viewPagerAdapter

        TabLayoutMediator(tablayoutPerfilesElemento,viewpagerPerfilesElemento) {tab, position ->
            when(position)
            {
                0 -> {
                    tab.text = "ELEMENTO"
                }
                1 -> {
                    tab.text = "LOCALIZADOR"
                }
            }
        }.attach()

        init()

        (activity as HomeActivity).setSupportActionBar(toolbaPerfilElemento)
        (activity as HomeActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbaPerfilElemento.title = ""


        return root
    }

    fun ocultar() {
        val vieww = (activity as HomeActivity)!!.currentFocus
        if (vieww != null) {
            //Aquí esta la magia
            val input =
                (activity as HomeActivity)!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(vieww.windowToken, 0)
        }
    }


    fun init() {

        perfilViewModel.pagina.observe(viewLifecycleOwner, Observer<String> {
            Toast.makeText(root.context, it, Toast.LENGTH_SHORT)
            if(it == "ELEMENTO") {
                nameElementoPerfil.text = elemento.nombre
                nameElementoPerfilAux.text = elemento.nombre
                civElementoPerfil.setImageResource(R.drawable.fondo_login)
                civElementoPerfilAux.setImageResource(R.drawable.fondo_login)
            }
            else {
                nameElementoPerfil.text = "Localizador"
                nameElementoPerfilAux.text = "Localizador"
                civElementoPerfil.setImageResource(R.drawable.sinotrack)
                civElementoPerfilAux.setImageResource(R.drawable.sinotrack)
            }
        })

    }

    /*fun loadFragment(fragment: Fragment) {
        tablayoutPerfilesElemento.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.teal_700), PorterDuff.Mode.SRC_IN);
        val fm = childFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame_perfiles_elemento, fragment)
        ft.commit()

    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Navigation.findNavController(root).navigate(R.id.action_perfilesElementoFragment_to_perfilesFragment)
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}