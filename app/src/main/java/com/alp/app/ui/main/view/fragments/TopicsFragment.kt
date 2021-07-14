/*
 * *
 *  * Created by estiv on 7/07/21 04:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 7/07/21 04:56 PM
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
import androidx.recyclerview.widget.GridLayoutManager
import com.alp.app.R
import com.alp.app.data.model.TopicsModel
import com.alp.app.databinding.FragmentTopicsBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.TopicsAdapter
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
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class TopicsFragment : Fragment() {

    private var _binding: FragmentTopicsBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private var idCourse = 0
    private lateinit var functions: Functions
    private var interstitial:InterstitialAd? = null
    private var count = 0
    private val args: TopicsFragmentArgs by navArgs()
    private val dashboardViewModel: DashboardViewModel by viewModels()
    lateinit var topicsAdapter: TopicsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTopicsBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        idCourse = args.idCourse
        Glide.with(contexto).load(args.imageCourse).into(binding.imageCourse)
        binding.titleCourse.text = args.nameCourse
        setupUI()
        setupShowData()
        initAds()
        initListeners()
        count += 1
        checkCounter()
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            topicsAdapter  = TopicsAdapter()
            val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) {
                        2
                    } else 1

                }
            }
            val gridLayoutManager = GridLayoutManager(context, 2)
            gridLayoutManager.spanSizeLookup = spanSizeLookup
            recycler.layoutManager = gridLayoutManager



            recycler.adapter = topicsAdapter
        }


    }

    private fun setupShowData() {
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.getTopics(idCourse, idUser!!).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: List<TopicsModel>) {
        if(data.isNotEmpty()){
            topicsAdapter.apply {
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
    private fun initAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireActivity(), resources.getString(R.string.intersitial_temary_ad_unit_id), adRequest, object : InterstitialAdLoadCallback(){
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }
}