/*
 * *
 *  * Created by estiv on 14/07/21, 1:27 a. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 1:27 a. m.
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.RepliesModel
import com.alp.app.databinding.FragmentRepliesBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.RepliesAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepliesFragment : Fragment() {
    private var _binding: FragmentRepliesBinding? = null
    private val binding get() = _binding!!
    private val args: RepliesFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var repliesAdapter: RepliesAdapter
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRepliesBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        functions = Functions(contexto)
        setupUI()
        setupShowData()
        with(binding){
            fullNameProfile.text = args.fullNameComment
            comment.text = args.comment
            date.text = args.dateComment
            if (args.imageComment.isNullOrEmpty()){
                Glide.with(contexto).load(R.drawable.ic_baseline_account_circle_24).into(binding.imageProfile)
            } else {
                Picasso.get()
                    .load(args.imageComment)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.imageProfile)
            }
        }
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            val dividerItemDecoration = DividerItemDecoration(contexto, LinearLayoutManager.VERTICAL)
            repliesAdapter  = RepliesAdapter()
            recyclerView.layoutManager = LinearLayoutManager(contexto)
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.adapter = repliesAdapter
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getReplies(1,args.idComment).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: List<RepliesModel>) {
        if (data.isNotEmpty()){
            repliesAdapter.apply {
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