/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 11:23 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.TopicsModel
import com.alp.app.databinding.TemplateCoursesTemaryBinding
import com.alp.app.ui.main.view.fragments.CoursesTemaryFragmentDirections
import com.bumptech.glide.Glide

class CoursesTemaryAdapter : RecyclerView.Adapter<CoursesTemaryAdapter.ViewHolder>() {

    companion object {
        val list = mutableListOf<TopicsModel>()
    }

    class ViewHolder(val binding: TemplateCoursesTemaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: TopicsModel) {
            /*with(binding){
                titleCourseTemary.text = data.nombre
                if (adapterPosition==0){
                    data.habilitado = "1"
                }
                if (data.habilitado == "1"){
                    Glide.with(itemView.context).load(R.drawable.ic_baseline_play_circle_24).into(iconoCursos)
                    cardView.isEnabled = true
                } else {
                    Glide.with(itemView.context).load(R.drawable.ic_baseline_lock_24).into(iconoCursos)
                    cardView.isEnabled = false
                }
            }
            itemView.setOnClickListener {
                val idTemary = data.id_temary
                val idCourse = data.idcurso
                val total = list.size
                val action = CoursesTemaryFragmentDirections.actionCoursesTemaryFragmentToCoursesTemaryDetailsFragment(idTemary, idCourse, total)
                it.findNavController().navigate(action)
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateCoursesTemaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun updateData(data: List<TopicsModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}