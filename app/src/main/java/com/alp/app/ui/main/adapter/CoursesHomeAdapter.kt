/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 09:31 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.CoursesModel
import com.alp.app.databinding.TemplateCoursesHomeBinding
import com.alp.app.ui.main.view.fragments.HomeFragmentDirections
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class CoursesHomeAdapter : RecyclerView.Adapter<CoursesHomeAdapter.ViewHolder>() {

    val list = mutableListOf<CoursesModel>()

    class ViewHolder(val binding: TemplateCoursesHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: CoursesModel) {
            itemView.setOnClickListener {
                val idCourse = data.id_course
                val name = data.title
                val image = data.image
                val action = HomeFragmentDirections.actionHomeFragmentToCoursesTemaryFragment(idCourse, name, image)
                it.findNavController().navigate(action)
            }
            Picasso.get()
                    .load(data.image).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(binding.imageCourse)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateCoursesHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun updateData(data: List<CoursesModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}
