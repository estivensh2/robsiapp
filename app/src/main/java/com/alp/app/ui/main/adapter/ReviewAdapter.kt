/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 28/06/21 06:42 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.ReviewModel
import com.alp.app.databinding.TemplateReviewBinding

class ReviewAdapter (val context: Context): RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    val list = ArrayList<ReviewModel>()
    var onItemClick: ((ReviewModel) -> Unit)? = null
    var boolean: Boolean? = null
    var optionSelected: Int = 0

    inner class ViewHolder(val binding: TemplateReviewBinding) : RecyclerView.ViewHolder(binding.root) {

        /*fun bindView(data: ReviewModel) {
            with(binding){
                question.text = data.question
                for (i in data.answers.indices) {
                    val radioButton = RadioButton(itemView.context)
                    radioButton.text = data.answers[i].option
                    radioButton.id = data.answers[i].id_option
                    option.addView(radioButton)
                }
                check.setOnClickListener {
                    optionSelected = option.checkedRadioButtonId
                    onItemClick?.invoke(data)
                }
            }
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bindView(list[position])
    }

    fun updateData(data: List<ReviewModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}