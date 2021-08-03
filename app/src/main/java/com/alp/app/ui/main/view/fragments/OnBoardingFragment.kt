/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/08/21, 10:53 p. m.
 *
 */

package com.alp.app.ui.main.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.alp.app.R
import com.alp.app.ui.main.adapter.OnBoardingAdapter
import com.alp.app.data.model.InduccionData
import com.alp.app.databinding.FragmentOnboardingBinding
import com.alp.app.singleton.PreferencesSingleton

class OnBoardingFragment : Fragment() {

    private val arrayList = ArrayList<InduccionData>()
    private val displayList = ArrayList<InduccionData>()
    private var _binding : FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        arrayList.add(InduccionData(R.drawable.ic_onboarding_1, getString(R.string.text_title_on_boarding_one), getString(R.string.text_description_on_boarding_one)))
        arrayList.add(InduccionData(R.drawable.ic_onboarding_2, getString(R.string.text_title_on_boarding_two), getString(R.string.text_description_on_boarding_two)))
        arrayList.add(InduccionData(R.drawable.ic_onboarding_3, getString(R.string.text_title_on_boarding_three), getString(R.string.text_description_on_boarding_three)))
        displayList.addAll(arrayList)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        val adapter = OnBoardingAdapter(requireContext(), displayList)
        binding.recyclerView.adapter = adapter
        binding.indicator.count = displayList.size
        with(binding){
            recyclerView.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    indicator.selection = position
                    if(position==2){
                        binding.skip.visibility = View.INVISIBLE
                        binding.finish.visibility = View.VISIBLE
                    } else {
                        binding.skip.visibility = View.VISIBLE
                        binding.finish.visibility = View.INVISIBLE
                    }
                }
            })
            finish.visibility = View.INVISIBLE
            skip.setOnClickListener { skipOnBoarding() }
            finish.setOnClickListener {
                PreferencesSingleton.write("user_new", true)
                PreferencesSingleton.write("enabled_sound", true)
                findNavController().navigate(R.id.accion_induccion_a_iniciar_o_crear)
            }
        }
        supportActionBar?.hide()
        return binding.root
    }

    private fun skipOnBoarding() {
        PreferencesSingleton.write("user_new", true)
        PreferencesSingleton.write("enabled_sound", true)
        findNavController().navigate(R.id.accion_induccion_a_iniciar_o_crear)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}