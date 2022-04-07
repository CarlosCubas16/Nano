package com.example.multigps.ui.perfiles.cu

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.multigps.Constant
import com.example.multigps.Models.Elemento
import com.example.multigps.Models.TipoElemento
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class PerfilesCuViewModel(var context: Context, var token: String): ViewModel() {

    var tipoFormulario = MutableLiveData<String>()

    var tipos_elemento_string = MutableLiveData<List<String>>()
    var tipos_elemento_ = MutableLiveData<List<TipoElemento>>(listOf()).apply {
        val request: StringRequest = object : StringRequest(
            Method.GET,
            Constant.TIPOS_ELEMTO,
            Response.Listener { response: String? ->
                try {
                    val `object` = JSONObject(response)
                    val respuesta: Boolean = `object`.getBoolean("success")
                    var tiposElemento:List<TipoElemento> = listOf()
                    var tiposElementoString: List<String> = listOf()
                    if (respuesta) {
                        val tipos_elementos_aux = `object`.getJSONArray("data")
                        for (i in 0 until tipos_elementos_aux.length()) {
                            val item = tipos_elementos_aux.getJSONObject(i)
                            tiposElementoString += listOf(item.getString("nombre"))
                            tiposElemento += listOf(
                                TipoElemento(
                                    item.getInt("id").toString(),
                                    item.getString("nombre"),
                                    item.getString("descripcion"),
                                    item.getString("estado")
                                )
                            )
                        }
                        value = tiposElemento
                        tipos_elemento_string.value = tiposElementoString
                    }
                    else
                    {

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                error.printStackTrace() }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["Content-Type"] = "application/json"
                map["X-Requested-With"] = "XMLHttpRequest"
                map["Authorization"] = "Bearer " + token
                return map
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    var tipos_elemento : MutableLiveData<List<TipoElemento>> = tipos_elemento_
}