/*
 * *
 *  * Created by estiv on 9/07/21 10:00 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 9/07/21 10:00 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.TopicsModel
import com.alp.app.databinding.TemplateTopicsBinding
import com.alp.app.ui.main.view.fragments.CoursesFragmentDirections
import com.alp.app.ui.main.view.fragments.HomeFragmentDirections
import com.alp.app.ui.main.view.fragments.TopicsFragmentDirections

class TopicsAdapter : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    companion object {
        val list = mutableListOf<TopicsModel>()
    }

    class ViewHolder(val binding: TemplateTopicsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: TopicsModel) {
            with(binding){
                titleTopic.text = data.title
            }
            itemView.setOnClickListener {
                val action = TopicsFragmentDirections.actionTopicsFragmentToDetailTopicFragment(data.id_topic, data.title)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateTopicsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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