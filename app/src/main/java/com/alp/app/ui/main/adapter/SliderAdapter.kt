/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 12:21 a. m.
 *
 */

package com.alp.app.ui.main.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.SliderModel
import com.alp.app.databinding.TemplateSliderBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class SliderAdapter : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    val list = ArrayList<SliderModel>()

    class ViewHolder(val binding: TemplateSliderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: SliderModel) {
            Picasso.get()
                    .load(data.image)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.imageSlider)
            itemView.setOnClickListener {
                itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.url)))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun updateData(data: List<SliderModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}