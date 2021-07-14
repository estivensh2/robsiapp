/*
 * *
 *  * Created by estiv on 12/07/21 02:07 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 12/07/21 02:07 AM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.data.model.CommentsCourseModel
import com.alp.app.databinding.FragmentCommentsCourseBinding
import com.alp.app.ui.main.adapter.CommentsCourseAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommentsCourseFragment : Fragment() {

    private var _binding: FragmentCommentsCourseBinding? = null
    private val binding get() = _binding!!
    private val args: CommentsCourseFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var commentsCourseAdapter: CommentsCourseAdapter
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCommentsCourseBinding.inflate(inflater, container, false)
        setupUI()
        setupShowData()
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            commentsCourseAdapter  = CommentsCourseAdapter()
            recyclerView.layoutManager = LinearLayoutManager(contexto)
            //recyclerView.addItemDecoration(DividerItemDecoration(contexto, DividerItemDecoration.HORIZONTAL))
            recyclerView.adapter = commentsCourseAdapter
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getComments(1,1).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerView.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderList(data: List<CommentsCourseModel>) {
        if (data.isNotEmpty()){
            commentsCourseAdapter.apply {
                updateData(data)
                notifyDataSetChanged()
            }
        } else {
            binding.textNoFound.apply {
                visibility = View.VISIBLE
                text = resources.getString(R.string.text_no_found)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

}