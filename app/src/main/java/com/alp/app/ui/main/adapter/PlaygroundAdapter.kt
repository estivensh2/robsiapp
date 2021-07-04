/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 3/07/21 09:22 PM
 *
 */

package com.alp.app.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.PlaygroundModel
import java.util.*

class PlaygroundAdapter(private val context: Context, private val words: ArrayList<PlaygroundModel>): RecyclerView.Adapter<PlaygroundAdapter.PageHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder  =
        PageHolder(LayoutInflater.from(context).inflate(R.layout.template_playground, parent, false))

    override fun onBindViewHolder(holder: PageHolder, position: Int) {

    }


    override fun getItemCount(): Int = words.size

    inner class PageHolder(view: View): RecyclerView.ViewHolder(view)
}