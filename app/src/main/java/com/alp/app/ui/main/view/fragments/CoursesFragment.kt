/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 05:40 PM
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.data.model.CoursesModel
import com.alp.app.databinding.FragmentCoursesBinding
import com.alp.app.ui.main.adapter.CoursesAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private var idCategory = 0
    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    private var interstitial:InterstitialAd? = null
    private var count = 0
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val args: CoursesFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private lateinit var functions: Functions
    private lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        idCategory = args.idCategory
        binding.tituloCategoria.text = args.nameCategory
        functions = Functions(contexto)
        Glide.with(contexto).load(args.imageCategory).into(binding.iconoCategoria)
        functions.showHideProgressBar(true, binding.progress)
        setupUI()
        setupShowData()
        initAds()
        initLoadAds()
        initListeners()
        count += 1
        checkCounter()
        return binding.root
    }

    private fun initListeners() {
        interstitial?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
            }
            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            }
            override fun onAdShowedFullScreenContent() {
                interstitial = null
            }
        }
    }

    private fun initLoadAds() {
        val adRequest = AdRequest.Builder().build()
        binding.banner.loadAd(adRequest)
    }


    private fun initAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireActivity(), resources.getString(R.string.intersitial_courses_ad_unit_id), adRequest, object : InterstitialAdLoadCallback(){
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitial = interstitialAd
            }
            override fun onAdFailedToLoad(p0: LoadAdError) {
                interstitial = null
            }
        })
    }

    private fun checkCounter() {
        if(count == 3){
            showAds()
            count = 0
            initAds()
        }
    }

    private fun showAds(){
        interstitial?.show(requireActivity())
    }

    private fun setupUI() {
        with(binding){
            coursesAdapter  = CoursesAdapter()
            recycler.layoutManager = LinearLayoutManager(contexto)
            recycler.adapter = coursesAdapter
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getCourses(idCategory).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.recycler.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        binding.recycler.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recycler.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderList(data: List<CoursesModel>) {
        coursesAdapter.apply {
            updateData(data)
            notifyDataSetChanged()
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