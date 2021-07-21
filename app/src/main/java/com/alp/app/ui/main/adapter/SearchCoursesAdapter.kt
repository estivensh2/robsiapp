/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 30/06/21 12:01 AM
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.CoursesModel
import com.alp.app.databinding.TemplateSearchCoursesBinding
import com.alp.app.ui.main.view.fragments.HomeFragmentDirections
import com.bumptech.glide.Glide
class SearchCoursesAdapter : RecyclerView.Adapter<SearchCoursesAdapter.ViewHolder>() {

    val list = ArrayList<CoursesModel>()

    class ViewHolder(val binding: TemplateSearchCoursesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: CoursesModel) {
            with(binding){
                titleCourse.text = data.title
                Glide.with(itemView.context).load(data.image).into(imageCourse)
                itemView.setOnClickListener {
                    val idCourse = data.id_course
                    val name = data.title
                    val image = data.image
                    val action = HomeFragmentDirections.actionHomeFragmentToTopicsFragment(idCourse, name, image)
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateSearchCoursesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
