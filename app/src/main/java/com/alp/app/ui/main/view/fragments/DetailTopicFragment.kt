/*
 * *
 *  * Created by estiv on 7/07/21 04:57 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 7/07/21 04:57 PM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.alp.app.data.model.DetailTopicModel
import com.alp.app.databinding.FragmentDetailTopicBinding
import com.alp.app.ui.main.adapter.DetailTopicAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTopicFragment : Fragment() {

    private var _binding: FragmentDetailTopicBinding? = null
    private val binding get() = _binding!!
    private val args: DetailTopicFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var detailTopicAdapter: DetailTopicAdapter
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailTopicBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        setupUI()
        //setupShowData()
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            detailTopicAdapter  = DetailTopicAdapter(requireActivity().supportFragmentManager, lifecycle)
            viewPager2.adapter  = detailTopicAdapter
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getDetailTopic(args.idTopic).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.viewPager2.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        binding.viewPager2.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.viewPager2.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderList(data: List<DetailTopicModel>) {
        detailTopicAdapter.updateData(data)
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