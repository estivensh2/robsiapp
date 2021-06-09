package com.alp.app.ui.main.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CategoryModel
import com.alp.app.databinding.TemplateCategoriesBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CategoriesAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    // obtenemos la lista de datos
    var list = ArrayList<CategoryModel>()
    // creamos la clase para mostrar los campos en la vista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TemplateCategoriesBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_categories, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        with(holder.binding){
            tituloCurso.text = list.name
            if (nuevo.text==""){
                nuevo.visibility = View.GONE
            } else {
                nuevo.text = context.getString(R.string.texto_nuevo)
                nuevo.visibility = View.VISIBLE
            }
            descripcionCurso.text = list.description
            Glide.with(context).load(list.icon).into(iconoCursos)
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            val id = list.id_category
            val nombre = list.name
            val icono = list.icon
            bundle.putString("id", id)
            bundle.putString("nombre", nombre)
            bundle.putString("icono", icono)
            Navigation.findNavController(it).navigate(R.id.accion_inicio_a_cursos, bundle)
        }
    }

    fun updateData(data: List<CategoryModel>) {
        this.list.apply {
            clear()
            addAll(data)
        }
    }
}