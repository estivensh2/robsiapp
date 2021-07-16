/*
 * *
 *  * Created by estiv on 14/07/21, 2:06 a. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 2:06 a. m.
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.RepliesModel
import com.alp.app.databinding.TemplateRepliesBinding
import com.alp.app.utils.Functions
import com.alp.app.utils.TimeAgo
import com.bumptech.glide.Glide
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class RepliesAdapter: RecyclerView.Adapter<RepliesAdapter.ViewHolder>() {

    val list = mutableListOf<RepliesModel>()

    class ViewHolder(val binding: TemplateRepliesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(data: RepliesModel){
            val functions = Functions(itemView.context)
            binding.reply.text = data.reply
            binding.fullNameProfile.text = data.full_name
            binding.date.text = TimeAgo.timeAgo(functions.convertDateToLong(data.date_created)!!)
            //binding.likes.text = data.likes.toString()
            if (data.image.isNullOrEmpty()){
                Glide.with(itemView.context).load(R.drawable.ic_baseline_account_circle_24).into(binding.imageProfile)
            } else {
                Picasso.get()
                    .load(data.image)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.imageProfile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateRepliesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(data: List<RepliesModel>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}