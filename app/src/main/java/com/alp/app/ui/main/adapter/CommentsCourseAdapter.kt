/*
 * *
 *  * Created by estiv on 13/07/21 07:14 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 13/07/21 07:14 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.CommentsCourseModel
import com.alp.app.databinding.TemplateCommentsBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class CommentsCourseAdapter : RecyclerView.Adapter<CommentsCourseAdapter.ViewHolder>() {

    val list = ArrayList<CommentsCourseModel>()

    class ViewHolder(val binding: TemplateCommentsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: CommentsCourseModel) {
            binding.comment.text = data.comment
            Picasso.get()
                    .load(data.image)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.imageProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun updateData(data: List<CommentsCourseModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}