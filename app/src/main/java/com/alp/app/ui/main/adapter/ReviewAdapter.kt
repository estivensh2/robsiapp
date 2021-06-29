package com.alp.app.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.ReviewModel
import com.alp.app.databinding.TemplateReviewBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ReviewAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    var list = ArrayList<ReviewModel>()
    var onItemClick: ((ReviewModel) -> Unit)? = null
    var boolean: Boolean? = null
    var optionSelected: Int = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TemplateReviewBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_review, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        with(holder.binding){
            question.text = list.question
            for (i in list.answers.indices) {
                val radioButton = RadioButton(context)
                radioButton.text = list.answers[i].option
                radioButton.id = list.answers[i].id_option
                option.addView(radioButton)
            }
            check.setOnClickListener {
                optionSelected = option.checkedRadioButtonId
                onItemClick?.invoke(list)
            }
        }
    }

    fun updateData(data: List<ReviewModel>) {
        this.list.apply {
            clear()
            addAll(data)
        }
    }
}