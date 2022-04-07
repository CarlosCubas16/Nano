package com.example.multigps.ui.perfiles

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class PerfilesViewModelFactory(private val context: Context, var token: String, var swipePerfiles: SwipeRefreshLayout) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilesViewModel::class.java)) {
            return PerfilesViewModel(context, token, swipePerfiles) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}