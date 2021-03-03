package com.alp.app.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.InduccionData
import com.alp.app.databinding.InduccionFilaBinding
import com.bumptech.glide.Glide


class InduccionAdaptador(val context: Context, val arrayList: ArrayList<InduccionData>) : RecyclerView.Adapter<InduccionAdaptador.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(modelo: InduccionData, context: Context){
            val binding = InduccionFilaBinding.bind(itemView)
            binding.tituloInduccion.text = modelo.titulo
            binding.descripcionInduccion.text = modelo.descripcion
            Glide.with(itemView).load(modelo.imagen).into(binding.imagenInduccion)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(context).inflate(R.layout.induccion_fila, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position], context)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}