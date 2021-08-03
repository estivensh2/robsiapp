/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 1/08/21, 6:00 p. m.
 *
 */

package com.alp.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.data.model.CertificateModel
import com.alp.app.databinding.TemplateCertificatesBinding
import com.alp.app.ui.main.view.fragments.CertificateFragmentDirections
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class CertificateAdapter : RecyclerView.Adapter<CertificateAdapter.ViewHolder>() {

    val list = ArrayList<CertificateModel>()

    class ViewHolder(val binding: TemplateCertificatesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: CertificateModel) {
            binding.title.text = data.name_course
            Picasso.get()
                    .load(data.image_course)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.image)
            itemView.setOnClickListener {
                val action = CertificateFragmentDirections.actionListadoDiplomadosFragmentToCertificatesDetailsFragment2(data.id_course)
                itemView.findNavController().navigate(action)
            }
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