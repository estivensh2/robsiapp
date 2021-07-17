/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 1/07/21 02:14 PM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.data.model.CategoryModel
import com.alp.app.data.model.CoursesModel
import com.alp.app.data.model.SliderModel
import com.alp.app.databinding.FragmentHomeBinding
import com.alp.app.ui.main.adapter.CategoriesAdapter
import com.alp.app.ui.main.adapter.CoursesHomeAdapter
import com.alp.app.ui.main.adapter.SearchCoursesAdapter
import com.alp.app.ui.main.adapter.SliderAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.gms.ads.AdRequest
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var contexto: Context
    private lateinit var functions: Functions
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var adapterCategories: CategoriesAdapter
    private lateinit var adapterSlider: SliderAdapter
    private lateinit var adapterCourses: CoursesHomeAdapter
    private lateinit var adapterSearchCourses: SearchCoursesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        setupUI()
        setupShowData()
        initLoadAds()
        binding.viewProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_profile2)
        }
        binding.searchView.setOnQueryTextListener(this)
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            adapterCategories = CategoriesAdapter()
            adapterSlider     = SliderAdapter()
            adapterCourses    = CoursesHomeAdapter()
            adapterSearchCourses    = SearchCoursesAdapter()
            recyclerViewSlider.adapter     = adapterSlider
            recyclerViewCourses.adapter    = adapterCourses
            recyclerViewCategories.adapter = adapterCategories
            recyclerViewSearchCourses.adapter = adapterSearchCourses
            recyclerViewCategories.layoutManager    = LinearLayoutManager(contexto)
            recyclerViewSearchCourses.layoutManager = LinearLayoutManager(contexto)
            recyclerViewSlider.layoutManager        = LinearLayoutManager(contexto, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewCourses.layoutManager       = LinearLayoutManager(contexto, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getCategories().observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recyclerViewCategories.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        binding.recyclerViewCategories.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerViewCategories.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
        dashboardViewModel.getSlider().observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recyclerViewSlider.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> retrieveSlider(data) }
                    }
                    Status.ERROR -> {
                        binding.recyclerViewSlider.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerViewSlider.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
        dashboardViewModel.getCoursesHome().observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recyclerViewCourses.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> retrieveCourses(data) }
                    }
                    Status.ERROR -> {
                        binding.recyclerViewCourses.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerViewCourses.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun retrieveSlider(list: List<SliderModel>) {
        adapterSlider.updateData(list)
    }


    private fun retrieveCourses(list: List<CoursesModel>) {
        Log.i("courses1", "$list")
        adapterCourses.updateData(list)
    }

    private fun renderList(data: List<CategoryModel>) {
        adapterCategories.apply {
            updateData(data)
            notifyDataSetChanged()
        }
    }

    private fun initLoadAds() {
        val adRequest = AdRequest.Builder().build()
        binding.banner.loadAd(adRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    private fun searchByName(search: String) {
        dashboardViewModel.searchCourses(search).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recyclerViewSearchCourses.visibility = View.VISIBLE
                        binding.nestedScrollView.visibility = View.GONE
                        binding.textNoFound.visibility = View.GONE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderSearchCourses(data) }
                    }
                    Status.ERROR -> {
                        binding.nestedScrollView.visibility = View.GONE
                        binding.recyclerViewSearchCourses.visibility = View.GONE
                        binding.textNoFound.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerViewSearchCourses.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun renderSearchCourses(list: List<CoursesModel>) {
        adapterSearchCourses.updateData(list)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty()){
            searchByName(newText.toLowerCase(Locale.ROOT))
        } else {
            binding.recyclerViewSearchCourses.visibility = View.GONE
            binding.textNoFound.visibility = View.GONE
            binding.nestedScrollView.visibility = View.VISIBLE
        }
        return true
    }
}