/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 11:15 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CoursesModel
import com.alp.app.databinding.TemplateCoursesBinding
import com.alp.app.ui.main.view.fragments.CoursesFragmentDirections
import com.alp.app.utils.Functions
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


class CoursesAdapter : RecyclerView.Adapter<CoursesAdapter.ViewHolder>() {

    val list = ArrayList<CoursesModel>()

    class ViewHolder(val binding: TemplateCoursesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(data: CoursesModel) {
            with(binding){
                title.text = data.title
                val functions = Functions(itemView.context)
                if (functions.converterDate(data.created_at)){
                    textNew.text = itemView.context.getString(R.string.text_new)
                    textNew.visibility = View.VISIBLE
                } else {
                    textNew.visibility = View.GONE
                }
                Picasso.get()
                        .load(data.image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_STORE)
                        .into(binding.image)
                itemView.setOnClickListener {
                    val idCourse = data.id_course
                    val name = data.title
                    val image = data.image
                    val action = CoursesFragmentDirections.actionCoursesFragmentToCoursesTemaryFragment(idCourse, name, image)
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateCoursesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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


