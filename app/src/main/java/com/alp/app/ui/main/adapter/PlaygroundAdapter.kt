/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 18/07/21, 7:05 p. m.
 *
 */

package com.alp.app.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alp.app.ui.main.view.fragments.languages.web.HtmlFragment
import com.alp.app.ui.main.view.fragments.languages.web.OutputFragment
import java.util.*

class PlaygroundAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    val list = ArrayList<Fragment>()

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return HtmlFragment()
            1 -> return OutputFragment()
        }
        return OutputFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}