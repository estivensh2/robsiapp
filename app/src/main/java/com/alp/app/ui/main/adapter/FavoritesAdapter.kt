/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 21/07/21, 6:48 p. m.
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.FavoritesModel
import com.alp.app.databinding.TemplateFavoritesBinding
import com.alp.app.ui.main.view.fragments.FavoritesFragmentDirections

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    val list = ArrayList<FavoritesModel>()

    class ViewHolder(val binding: TemplateFavoritesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(data: FavoritesModel) {
            with(binding){
                titleCourse.text = data.title_course
                titleDetailTopic.text = data.title_detail_topic
            }
            itemView.setOnClickListener {
                val action = FavoritesFragmentDirections.actionFavoritesFragmentToFavoritesDetailsFragment(data.id_detail_topic)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun updateData(data: List<FavoritesModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}