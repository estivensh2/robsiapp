package com.alp.app.adaptadores

import android.R.attr.resource
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.RespuestaSliderData
import com.alp.app.databinding.SliderPaginaBinding
import com.bumptech.glide.Glide


class SliderAdaptador(val arrayList: ArrayList<RespuestaSliderData>, val contexto: Context): RecyclerView.Adapter<SliderAdaptador.ViewHolder>()  {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bintItems(modelo: RespuestaSliderData, contexto: Context){
            val binding = SliderPaginaBinding.bind(itemView)
            binding.tituloSlider.text = modelo.titulo
            binding.descripcionSlider.text = modelo.descripcion
            Glide.with(contexto).load(modelo.imagen).into(binding.imagenFondo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.slider_pagina, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItems(arrayList[position], contexto)

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}