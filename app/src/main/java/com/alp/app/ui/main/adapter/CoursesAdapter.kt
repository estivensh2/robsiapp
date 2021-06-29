package com.alp.app.ui.main.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CoursesModel
import com.alp.app.databinding.TemplateCoursesBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CoursesAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<CoursesAdapter.ViewHolder>() {
    // obtenemos la lista de datos
    var list = ArrayList<CoursesModel>()
    // creamos la clase para mostrar los campos en la vista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TemplateCoursesBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_courses, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        with(holder.binding){
            tituloCursox.text = list.name
            if (nuevo.text==""){
                nuevo.visibility = View.GONE
            } else {
                nuevo.text = context.getString(R.string.text_new)
                nuevo.visibility = View.VISIBLE
            }
            Glide.with(context).load(list.image).into(iconoCursos)
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            val id = list.id_course
            val nombre = list.name
            val icono = list.image
            bundle.putString("id", id)
            bundle.putString("nombre", nombre)
            bundle.putString("icono", icono)
            Navigation.findNavController(it).navigate(R.id.accion_inicio_cursos_a_detalle, bundle)
        }
    }

    fun updateData(users: List<CoursesModel>) {
        this.list.apply {
            clear()
            addAll(users)
        }
    }
}
