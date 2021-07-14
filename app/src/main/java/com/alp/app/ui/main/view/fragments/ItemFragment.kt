/*
 * *
 *  * Created by estiv on 7/07/21 04:54 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 7/07/21 04:54 PM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.alp.app.R
import com.alp.app.data.model.DetailTopicModel
import com.alp.app.databinding.FragmentItemBinding
import com.alp.app.databinding.TemplateReportBinding
import com.alp.app.utils.Functions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ItemFragment : Fragment(){

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var functions: Functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        functions = Functions(requireContext())
        Toast.makeText(context, "$param1", Toast.LENGTH_SHORT).show()

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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(data: DetailTopicModel) = ItemFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, data.description)
            }
        }
    }
}