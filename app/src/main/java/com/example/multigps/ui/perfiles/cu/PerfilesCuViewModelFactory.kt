package com.example.multigps.ui.perfiles.cu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PerfilesCuViewModelFactory(var context: Context, var token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilesCuViewModel::class.java)) {
            return PerfilesCuViewModel(context, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}