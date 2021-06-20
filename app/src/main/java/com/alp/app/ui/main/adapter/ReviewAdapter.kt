package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.Questions
import com.alp.app.data.model.ReviewModel
import com.alp.app.databinding.TemplateSliderBinding
import javax.inject.Inject

class ReviewAdapter @Inject constructor() : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    val listData = ArrayList<ReviewModel>()

    class ViewHolder(val itemBinding : TemplateSliderBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(data: ReviewModel){
            itemBinding.tituloSlider.text = data.data.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listData[position])
    }

    fun updateData(data: ArrayList<ReviewModel>) {
        listData.clear()
        listData.addAll(data)
        notifyDataSetChanged()
    }
}