package com.alp.app.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.SliderModel
import com.alp.app.databinding.TemplateSliderBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SliderAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {
    // obtenemos la lista de datos
    var list = ArrayList<SliderModel>()
    // creamos la clase para mostrar los campos en la vista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TemplateSliderBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_slider, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        with(holder.binding){
            tituloSlider.text = list.titulo
            descripcionSlider.text = list.descripcion
            Glide.with(context).load(list.imagen).into(imagenFondo)
        }
    }

    fun updateData(users: List<SliderModel>) {
        this.list.apply {
            clear()
            addAll(users)
        }
    }
}