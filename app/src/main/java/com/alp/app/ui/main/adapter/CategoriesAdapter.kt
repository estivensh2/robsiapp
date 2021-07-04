/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 30/06/21 01:45 AM
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CategoryModel
import com.alp.app.databinding.TemplateCategoriesBinding
import com.alp.app.ui.main.view.fragments.HomeFragmentDirections
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    val list = ArrayList<CategoryModel>()

    class ViewHolder(val binding: TemplateCategoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: CategoryModel) {
            with(binding){
                titleCategory.text = data.name
                if (textNew.text==""){
                    textNew.visibility = View.GONE
                } else {
                    textNew.text = itemView.context.getString(R.string.text_new)
                    textNew.visibility = View.VISIBLE
                }
                descriptionCategory.text = data.description
                Picasso.get()
                        .load(data.icon)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_STORE)
                        .into(imageCategory)
            }
            itemView.setOnClickListener {
                val idCategory = data.id_category
                val name = data.name
                val image = data.icon
                val action = HomeFragmentDirections.actionHomeFragmentToCoursesFragment(idCategory, name, image)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun updateData(data: List<CategoryModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}