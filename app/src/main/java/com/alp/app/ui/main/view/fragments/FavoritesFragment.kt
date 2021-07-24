/*
 * *
 *  * Created by estiv on 21/07/21, 12:45 a. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 21/07/21, 12:45 a. m.
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.data.model.FavoritesModel
import com.alp.app.databinding.FragmentFavoritesBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.*
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private lateinit var functions: Functions
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        setupUI()
        setupShowData()
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            favoritesAdapter = FavoritesAdapter()
            recycler.adapter = favoritesAdapter
            recycler.layoutManager = LinearLayoutManager(contexto)
        }
    }

    private fun setupShowData() {
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.getFavorites(idUser!!).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun renderList(data: List<FavoritesModel>) {
        if (data.isNotEmpty()) {
            favoritesAdapter.updateData(data)
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