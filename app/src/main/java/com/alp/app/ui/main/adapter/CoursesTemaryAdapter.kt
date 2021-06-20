package com.alp.app.ui.main.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CoursesTemaryModel
import com.alp.app.databinding.TemplateCoursesTemaryBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CoursesTemaryAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<CoursesTemaryAdapter.ViewHolder>() {
    // obtenemos la lista de datos
    var lista = ArrayList<CoursesTemaryModel>()
    // creamos la clase para mostrar los campos en la vista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TemplateCoursesTemaryBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_courses_temary, parent, false))
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = lista[position]
        with(holder.binding){
            tituloCursox.text = list.nombre
            if (position==0){
                list.habilitado = "1"
            }
            if (list.habilitado == "1"){
                Glide.with(context).load(R.drawable.unlocked).into(iconoCursos)
                cardViewCursosDetalle.isEnabled = true
            } else {
                Glide.with(context).load(R.drawable.locked).into(iconoCursos)
                //cardViewCursosDetalle.isEnabled = false
            }
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            val id = list.id
            val idcurso = list.idcurso
            val nombre = list.nombre
            val habil = list.habilitado
            val descripcion = list.descripcion
            val codigo = list.codigo
            val tipolenguaje = list.tipolenguaje
            val imgresultado = list.imgresultado
            bundle.putInt("id", id)
            bundle.putInt("idcurso", idcurso)
            bundle.putString("nombre", nombre)
            bundle.putString("habilitado", habil)
            bundle.putString("descripcion", descripcion)
            bundle.putString("codigo", codigo)
            bundle.putString("tipolenguaje", tipolenguaje)
            bundle.putString("imgresultado", imgresultado)
            bundle.putString("total", lista.size.toString())
            bundle.putInt("posicion", position)
            bundle.putString("ultimoelemento", lista.last().total)
            //bundle.putString("ultimoelemento", list.toString())
            Navigation.findNavController(it).navigate(R.id.accion_detalle_a_temario, bundle)
        }
    }

    fun updateData(users: List<CoursesTemaryModel>) {
        this.lista.apply {
            clear()
            addAll(users)
        }
    }
}