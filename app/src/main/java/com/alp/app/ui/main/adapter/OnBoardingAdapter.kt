/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 12:21 a. m.
 *
 */

package com.alp.app.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.InduccionData
import com.alp.app.databinding.TemplateOnboardingBinding
import com.bumptech.glide.Glide

class OnBoardingAdapter(val context: Context, private val arrayList: ArrayList<InduccionData>) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {

    class ViewHolder(val binding: TemplateOnboardingBinding): RecyclerView.ViewHolder(binding.root){
        fun bindView(data: InduccionData){
            val binding = TemplateOnboardingBinding.bind(itemView)
            binding.title.text = data.titulo
            binding.description.text = data.descripcion
            Glide.with(itemView).load(data.imagen).into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(arrayList[position])
    }

    override fun getItemCount(): Int = arrayList.size
}