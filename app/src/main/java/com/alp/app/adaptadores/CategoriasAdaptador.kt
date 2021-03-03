package com.alp.app.adaptadores

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.RespuestaCategoriaData
import com.alp.app.databinding.TarjetasCategoriasCursosBinding
import com.bumptech.glide.Glide

class CategoriasAdaptador(val arrayList: ArrayList<RespuestaCategoriaData>, val contexto: Context): RecyclerView.Adapter<CategoriasAdaptador.ViewHolder>()  {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bintItems(modelo: RespuestaCategoriaData, contexto: Context){
            val binding = TarjetasCategoriasCursosBinding.bind(itemView)
            binding.tituloCurso.text = modelo.nombre
            if (binding.nuevo.text==""){
                binding.nuevo.visibility = View.GONE
            } else {
                binding.nuevo.text = "Nuevo"
                binding.nuevo.visibility = View.VISIBLE
            }
            binding.descripcionCurso.text = modelo.descripcion
            binding.fondoCategorias.setBackgroundColor(Color.parseColor(modelo.fondo))
            Glide.with(contexto).load(modelo.icono).into(binding.iconoCursos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.tarjetas_categorias_cursos, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItems(arrayList[position], contexto)
        holder.itemView.setOnClickListener { v ->
            val bundle = Bundle()
            val modelo = arrayList.get(position)
            val id = modelo.id
            val nombre = modelo.nombre
            val fondo = modelo.fondo
            val icono = modelo.icono
            bundle.putString("id", id)
            bundle.putString("nombre", nombre)
            bundle.putString("fondo", fondo)
            bundle.putString("icono", icono)
            Navigation.findNavController(v).navigate(R.id.accion_inicio_a_cursos, bundle)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}