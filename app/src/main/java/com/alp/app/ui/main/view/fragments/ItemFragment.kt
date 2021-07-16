/*
 * *
 *  * Created by estiv on 7/07/21 04:54 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 7/07/21 04:54 PM
 *
 */

package com.alp.app.ui.main.view.fragments


import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.alp.app.databinding.FragmentItemBinding
import com.alp.app.utils.Functions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class ItemFragment : Fragment(){

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        functions = Functions(requireContext())
        binding.comments.setOnClickListener {
            val action = DetailTopicFragmentDirections.actionDetailTopicFragmentToCommentsCourseFragment(1,1)
            it.findNavController().navigate(action)
        }
        return binding.root
    }

    private fun showSnackBar(text: String){
        val snackBar = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
        val layoutParams = LinearLayout.LayoutParams(snackBar.view.layoutParams)
        layoutParams.gravity = Gravity.TOP
        snackBar.view.setPadding(0, 10, 0, 0)
        snackBar.view.layoutParams = layoutParams
        snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBar.show()
    }
}