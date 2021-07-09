/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 6/06/21 05:31 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.CertificateModel
import com.alp.app.databinding.TemplateCertificatesBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class CertificateAdapter : RecyclerView.Adapter<CertificateAdapter.ViewHolder>() {

    val list = ArrayList<CertificateModel>()

    class ViewHolder(val binding: TemplateCertificatesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: CertificateModel) {
            binding.title.text = data.name_course
            binding.btnDownload.setOnClickListener {
                itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.url_download)))
            }
            Picasso.get()
                    .load(data.image_course)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TemplateCertificatesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun updateData(data: List<CertificateModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}