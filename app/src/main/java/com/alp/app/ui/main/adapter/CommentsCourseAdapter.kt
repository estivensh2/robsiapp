/*
 * *
 *  * Created by estiv on 13/07/21 07:14 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 13/07/21 07:14 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CommentsCourseModel
import com.alp.app.databinding.TemplateCommentsBinding
import com.alp.app.ui.main.view.fragments.CommentsCourseFragmentDirections
import com.alp.app.utils.Functions
import com.alp.app.utils.TimeAgo
import com.bumptech.glide.Glide
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.util.*

class CommentsCourseAdapter(var itemClickListener: ItemClickListener, var likeClickListener: LikeClickListener) : RecyclerView.Adapter<CommentsCourseAdapter.ViewHolder>() {

    val list = ArrayList<CommentsCourseModel>()

    inner class ViewHolder(val binding: TemplateCommentsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: CommentsCourseModel) {
            val functions = Functions(itemView.context)
            binding.comment.text = data.comment
            binding.fullNameProfile.text = data.full_name
            if (functions.compareDates(data.date_created, data.date_updated)){
                binding.textEdited.visibility = View.VISIBLE
            }
            binding.likeComment.isChecked = data.active_my_like == 1
            binding.date.text = TimeAgo.timeAgo(functions.convertDateToLong(data.date_created)!!)
            binding.likes.text = data.likes.toString()
            if (data.replies > 0){
                if (data.replies == 1){
                    binding.replies.text = itemView.context.resources.getString(R.string.text_one_reply, data.replies)
                } else {
                    binding.replies.text = itemView.context.resources.getString(R.string.text_quantity_replies, data.replies)
                }
                binding.replies.setOnClickListener {
                    val action = CommentsCourseFragmentDirections.actionCommentsCourseFragmentToRepliesFragment(
                        data.id_comment,
                        data.full_name,
                        data.image,
                        data.comment,
                        TimeAgo.timeAgo(functions.convertDateToLong(data.date_created)!!)
                    )
                    it.findNavController().navigate(action)
                }
            } else {
                binding.replies.setOnClickListener {
                    val action = CommentsCourseFragmentDirections.actionCommentsCourseFragmentToRepliesFragment(
                        data.id_comment,
                        data.full_name,
                        data.image,
                        data.comment,
                        TimeAgo.timeAgo(functions.convertDateToLong(data.date_created)!!)
                    )
                    it.findNavController().navigate(action)
                }
                binding.replies.text = itemView.context.resources.getString(R.string.text_no_replies)
            }
            if (data.image.isNullOrEmpty()){
                Glide.with(itemView.context).load(R.drawable.ic_baseline_account_circle_24).into(binding.imageProfile)
            } else {
                Picasso.get()
                    .load(data.image)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.imageProfile)
            }
            binding.setting.setOnClickListener {
                itemClickListener.itemClick(data, adapterPosition, itemView)
            }
            var likes = binding.likes.text.toString().toInt()
            binding.likeComment.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    if (likes == 0){
                        binding.likes.text = "" + 1
                    } else {
                        binding.likes.text = "" + ++likes
                    }
                    likeClickListener.itemClick(data, true)
                } else {
                    if (likes == 0){
                        binding.likes.text = "" + 0
                    } else {
                        binding.likes.text = "" + --likes
                    }
                    likeClickListener.itemClick(data, false)
                }
            }
        }
    }

    interface ItemClickListener {
        fun itemClick(data: CommentsCourseModel, position: Int, view: View)
    }

    interface LikeClickListener {
        fun itemClick(data: CommentsCourseModel, boolean: Boolean)
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
        notifyItemChanged(1)
    }


}