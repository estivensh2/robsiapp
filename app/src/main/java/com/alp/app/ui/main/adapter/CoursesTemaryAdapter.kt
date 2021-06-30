package com.alp.app.ui.main.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CoursesTemaryModel
import com.alp.app.databinding.TemplateCoursesTemaryBinding
import com.alp.app.ui.main.view.fragments.CoursesFragmentDirections
import com.alp.app.ui.main.view.fragments.CoursesTemaryFragmentDirections
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CoursesTemaryAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<CoursesTemaryAdapter.ViewHolder>() {

    var lista = ArrayList<CoursesTemaryModel>()

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
                Glide.with(context).load(R.drawable.ic_baseline_play_circle_24).into(iconoCursos)
                cardViewCursosDetalle.isEnabled = true
            } else {
                Glide.with(context).load(R.drawable.ic_baseline_lock_24).into(iconoCursos)
                //cardViewCursosDetalle.isEnabled = false
            }
        }
        holder.itemView.setOnClickListener {
            val idTemary = list.id_temary
            val idCourse = list.idcurso
            val total = lista.size
            val action = CoursesTemaryFragmentDirections.actionCoursesTemaryFragmentToCoursesTemaryDetailsFragment(idTemary, idCourse, total)
            it.findNavController().navigate(action)
        }
    }

    fun updateData(users: List<CoursesTemaryModel>) {
        this.lista.apply {
            clear()
            addAll(users)
        }
    }
}