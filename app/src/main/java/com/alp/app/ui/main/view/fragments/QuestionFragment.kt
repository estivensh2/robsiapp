/*
 * *
 *  * Created by estiv on 7/07/21 04:54 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 7/07/21 04:54 PM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alp.app.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }
}