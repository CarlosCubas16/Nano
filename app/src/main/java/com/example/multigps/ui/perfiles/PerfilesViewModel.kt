package com.example.multigps.ui.perfiles

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.multigps.Constant
import com.example.multigps.Models.Elemento
import com.example.multigps.Models.TipoElemento
import com.example.multigps.ProviderType
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class PerfilesViewModel(var context: Context, var token: String, var swipePerfiles: SwipeRefreshLayout): ViewModel() {
    var elementos = MutableLiveData<List<Elemento>>()

    init {
        loadPerfiles()
    }

    fun loadPerfiles()
    {
        swipePerfiles.isRefreshing = true
        val request: StringRequest = object : StringRequest(
            Method.GET,
            Constant.ELEMENTOS,
            Response.Listener { response: String? ->
                try {
                    val `object` = JSONObject(response)
                    val respuesta: Boolean = `object`.getBoolean("success")
                    var elementos_aux:List<Elemento> = listOf()
                    if (respuesta) {
                        val elementos_= `object`.getJSONArray("data")
                        for (i in 0 until elementos_.length()) {
                            val item = elementos_.getJSONObject(i)
                            val tipo = item.getJSONObject("tipo_elemento")
                            val tipoElemento = TipoElemento(
                                tipo.getInt("id").toString(),
                                tipo.getString("nombre"),
                                tipo.getString("descripcion"),
                                tipo.getString("estado")
                            )
                            elementos_aux += listOf(
                                Elemento(
                                    item.getInt("id").toString(),
                                    item.getInt("tipo_elemento_id").toString(),
                                    item.getInt("cliente_id").toString(),
                                    item.getString("nombre"),
                                    item.getString("descripcion"),
                                    item.getString("peso"),
                                    item.getDouble("edad").toString(),
                                    item.getString("fecha_nacimiento"),
                                    item.getString("altura"),
                                    item.getString("estado"),
                                    item.getString("url_imagen"),
                                    item.getString("nombre_imagen"),
                                    item.getString("estado_asignacion"),
                                    tipoElemento
                                )
                            )
                        }
                        elementos.value = elementos_aux

                        swipePerfiles.isRefreshing = false
                    }
                    else
                    {
                        swipePerfiles.isRefreshing = false
                    }
                } catch (e: JSONException) {
                    swipePerfiles.isRefreshing = false
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                swipePerfiles.isRefreshing = false
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
}