package com.example.multigps.ui.perfiles.elemento

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.multigps.Models.Elemento

class PerfilViewModel:ViewModel() {
    var elemento = MutableLiveData<Elemento>()
    var pagina = MutableLiveData<String>()
    var position = MutableLiveData<Int>()

    init {
        position.value = 0
    }

    fun changePagina(cadena: String) {
        Log.i("Pagina", cadena)
        pagina.value = cadena
    }

}