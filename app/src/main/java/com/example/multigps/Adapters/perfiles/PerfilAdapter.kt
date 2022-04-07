package com.example.multigps.Adapters.perfiles

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.multigps.Models.Elemento
import com.example.multigps.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator

class PerfilAdapter(var context: Context, elementos_: List<Elemento>): RecyclerView.Adapter<PerfilAdapter.ViewHolder>(),
    Filterable {

    var elementos: List<Elemento>
    var elementosAll: List<Elemento>
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val card_perfil: MaterialCardView
        val image_perfil: ImageView
        val nombre_perfil: TextView
        val descripcion_perfil: TextView
        val edad_perfil: TextView
        val peso_perfil: TextView
        //val progress: CircularProgressIndicator
        init {
            card_perfil = itemView.findViewById(R.id.cardPerfil)
            image_perfil = itemView.findViewById(R.id.imagePerfil)
            nombre_perfil = itemView.findViewById(R.id.nombrePerfil)
            descripcion_perfil = itemView.findViewById(R.id.descripcionPerfil)
            edad_perfil = itemView.findViewById(R.id.edadPerfil)
            peso_perfil = itemView.findViewById(R.id.pesoPerfil)
            //progress = itemView.findViewById(R.id.progress_img_producto)
        }
    }

    init {
        this.context = context
        this.elementos = elementos_
        this.elementosAll = elementos_
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_perfil,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PerfilAdapter.ViewHolder, position: Int) {
        holder.nombre_perfil.text = elementos.get(position).nombre
        holder.descripcion_perfil.text = elementos.get(position).descripcion
        holder.edad_perfil.text = "Edad: " + elementos.get(position).edad
        holder.peso_perfil.text = "Peso: " + elementos.get(position).peso
        holder.card_perfil.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("elemento", elementos[position])
            Navigation.findNavController(it).navigate(R.id.action_perfilesFragment_to_perfilesElementoFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return  elementos.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (constraint.toString().isEmpty()) {
                    elementos = elementosAll
                } else {
                    val filteredList: MutableList<Elemento> = java.util.ArrayList<Elemento>()
                    for (elemento in elementosAll) {
                        if (elemento.descripcion!!.toLowerCase()
                                .contains(constraint.toString().toLowerCase()) ||
                            elemento.nombre!!.toLowerCase()
                                .contains(constraint.toString().toLowerCase()) ||
                            elemento.tipo_elemento!!.nombre!!.toLowerCase()
                                .contains(constraint.toString().toLowerCase())
                        ){
                            filteredList.add(elemento)
                        }
                    }
                    elementos = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = elementos

                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                elementos = results?.values as ArrayList<Elemento>
                notifyDataSetChanged()
            }

        }
    }
}