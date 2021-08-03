/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 30/07/21, 6:41 p. m.
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.TopicsModel
import com.alp.app.databinding.TemplateTopicsBinding
import retrofit2.Response

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
                itemsCompleted.text = data.items
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