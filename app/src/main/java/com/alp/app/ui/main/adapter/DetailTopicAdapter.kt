/*
 * *
 *  * Created by estiv on 12/07/21 10:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 12/07/21 10:28 PM
 *
 */

package com.alp.app.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.alp.app.data.model.DetailTopicModel
import com.alp.app.ui.main.view.fragments.ItemFragment

class DetailTopicAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    val list = ArrayList<DetailTopicModel>()

    override fun createFragment(position: Int): Fragment {
        //return ItemFragment.newInstance(list[position])
        return ItemFragment()
    }

    override fun getItemCount(): Int {
        return 3
    }

    fun updateData(data: List<DetailTopicModel>){
        list.addAll(data)
    }
}

