package com.alp.app.adaptadores

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.RespuestaCategoriaData
import com.alp.app.data.RespuestaExamen
import com.alp.app.databinding.TarjetasCategoriasCursosBinding
import com.bumptech.glide.Glide

class ExamenAdaptador(private val arrayList: ArrayList<RespuestaExamen>, private val contexto: Context): RecyclerView.Adapter<ExamenAdaptador.ViewHolder>()  {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bintItems(modelo: RespuestaExamen, contexto: Context){
            val binding = TarjetasCategoriasCursosBinding.bind(itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.tarjetas_categorias_cursos, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItems(arrayList[position], contexto)
        holder.itemView.setOnClickListener { v ->
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}