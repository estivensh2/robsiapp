/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/08/21, 10:05 p. m.
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
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

class RepliesAdapter(var itemClickListener: ItemClickListener, var likeClickListener: LikeClickListener): RecyclerView.Adapter<RepliesAdapter.ViewHolder>() {

    val list = mutableListOf<RepliesModel>()

    inner class ViewHolder(val binding: TemplateRepliesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(data: RepliesModel){
            val functions = Functions(itemView.context)
            binding.reply.text = data.reply
            binding.fullNameProfile.text = data.full_name
            if (functions.compareDates(data.date_created, data.date_updated)){
                binding.textEdited.visibility = View.VISIBLE
            }
            binding.date.text = TimeAgo.timeAgo(functions.convertDateToLong(data.date_created)!!)
            binding.separator.visibility = View.GONE
            binding.likes.text = data.likes.toString()
            if (list.size > 1){
                binding.separator.visibility = View.VISIBLE
            } else {
                binding.separator.visibility = View.GONE
            }
            binding.likeComment.isChecked = data.active_my_like == 1
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
                        binding.likes.text = itemView.resources.getString(R.string.text_plush_likes, 1)
                    } else {
                        binding.likes.text = itemView.resources.getString(R.string.text_plush_likes, ++likes)
                    }
                    likeClickListener.itemClick(data, true)
                } else {
                    if (likes == 0){
                        binding.likes.text = itemView.resources.getString(R.string.text_plush_likes, 0)
                    } else {
                        binding.likes.text = itemView.resources.getString(R.string.text_plush_likes, --likes)
                    }
                    likeClickListener.itemClick(data, false)
                }
            }
        }
    }

    interface ItemClickListener {
        fun itemClick(data: RepliesModel, position: Int, view: View)
    }

    interface LikeClickListener {
        fun itemClick(data: RepliesModel, boolean: Boolean)
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