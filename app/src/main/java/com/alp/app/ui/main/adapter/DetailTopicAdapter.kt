/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/07/21, 7:16 p. m.
 *
 */

package com.alp.app.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.alp.app.data.model.DetailTopicModel
import com.alp.app.ui.main.view.fragments.BlankFragment
import com.alp.app.ui.main.view.fragments.ItemFragment

class DetailTopicAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val idCourse: Int) : FragmentStateAdapter(fragmentManager, lifecycle) {

    val list = ArrayList<DetailTopicModel>()

    override fun createFragment(position: Int): Fragment {
        return if (position% 2 == 0) {
            ItemFragment.newInstance(list[position], position, idCourse)
        } else {
            BlankFragment.newInstance(list[position], position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(data: List<DetailTopicModel>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}

