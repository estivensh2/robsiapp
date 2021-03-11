package com.alp.app.adaptadores

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.RespuestaCursosData
import com.alp.app.databinding.TarjetasCursosBinding
import com.bumptech.glide.Glide

class CursosAdaptador (private val arrayList: ArrayList<RespuestaCursosData>, private val contexto: Context): RecyclerView.Adapter<CursosAdaptador.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bintItems(modelo: RespuestaCursosData, contexto: Context){
            val binding = TarjetasCursosBinding.bind(itemView)
            binding.tituloCursox.text = modelo.nombre
            Glide.with(contexto).load(modelo.imagen).into(binding.iconoCursos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.tarjetas_cursos, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItems(arrayList[position], contexto)
        holder.itemView.setOnClickListener { v ->
            val bundle = Bundle()
            val modelo = arrayList[position]
            val id = modelo.id
            val nombre = modelo.nombre
            val icono = modelo.imagen
            bundle.putString("id", id)
            bundle.putString("nombre", nombre)
            bundle.putString("icono", icono)
            Navigation.findNavController(v).navigate(R.id.accion_inicio_cursos_a_detalle, bundle)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}