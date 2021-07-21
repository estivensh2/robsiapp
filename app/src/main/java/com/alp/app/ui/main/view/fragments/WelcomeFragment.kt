/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 01:55 AM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.databinding.FragmentWelcomeBinding
import com.alp.app.singleton.PreferencesSingleton
import com.bumptech.glide.Glide

class WelcomeFragment : Fragment() {

    private var _binding : FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val time: Long = 500

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.hide()
        createAnimation()
        return binding.root
    }

    private fun createAnimation() {
        val url = ContextCompat.getDrawable(requireContext(), R.drawable.ic_splash_screen)
        Glide.with(this).load(url).into(binding.logo)
        Handler(Looper.getMainLooper()).postDelayed({
            validateStatusSessionAndWelcome()
        }, time)
    }

    private fun validateStatusSessionAndWelcome() {
        if (PreferencesSingleton.read("user_new", false) == true) {
            if (PreferencesSingleton.read("active_session", false) == true){
                lifecycleScope.launchWhenResumed {
                    findNavController().navigate(R.id.action_welcomeFragment_to_principalActivity2)
                }
                activity?.finish()
            } else {
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }
        } else {
            findNavController().navigate(R.id.action_welcomeFragment_to_onBoardingFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}