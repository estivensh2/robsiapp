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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.TopicsModel
import com.alp.app.databinding.TemplateTopicsBinding

class TopicsAdapter(var itemClickListener: ItemClickListener) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    companion object {
        val list = mutableListOf<TopicsModel>()
    }

    inner class ViewHolder(val binding: TemplateTopicsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: TopicsModel) {
            with(binding){
                titleTopic.text = data.title
                progressBar.progress = data.percentage
                if (data.percentage == 100){
                    imageButton.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_check_24)
                }
            }
            itemView.setOnClickListener {
                itemClickListener.itemClick(data)
            }
        }
    }

    interface ItemClickListener {
        fun itemClick(data: TopicsModel)
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