/*
 * *
 *  * Created by estiv on 7/07/21 04:57 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 7/07/21 04:57 PM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alp.app.databinding.FragmentDetailTopicBinding
import com.alp.app.ui.main.adapter.BlankFragment
import com.google.android.material.tabs.TabLayoutMediator


class DetailTopicFragment : Fragment() {

    private var _binding: FragmentDetailTopicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailTopicBinding.inflate(inflater, container, false)

        val players: ArrayList<String> = ArrayList()
        val courses: ArrayList<String> = ArrayList()
        players.add("titulo1")
        players.add("titulo2")
        players.add("titulo3")
        players.add("titulo4")

        courses.add("course1")
        courses.add("course2")
        courses.add("course3")
        courses.add("course4")
        val adapter = ViewPagerFragmentAdapter(requireActivity().supportFragmentManager, lifecycle, players, courses)
        binding.viewPager2.adapter = adapter
        return binding.root
    }


    class ViewPagerFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, players: ArrayList<String>, courses: ArrayList<String>) : FragmentStateAdapter(fragmentManager, lifecycle) {

        private val arrayList: ArrayList<Fragment> = ArrayList()

        private val players: ArrayList<String> = players
        private val courses: ArrayList<String> = courses


        fun addFragment(fragment: Fragment) {
            arrayList.add(fragment)
        }

        override fun getItemCount(): Int {
            return players.size
        }

        override fun createFragment(position: Int): Fragment {
            when(position % 2) {
                0 -> addFragment(ItemFragment.newInstance(courses[position]))
                1 -> addFragment(BlankFragment.newInstance(players[position]))
            }
            return arrayList[position]
        }
    }
}