/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/07/21 09:53 PM
 *
 */

package com.alp.app.ui.main.view.fragments.languages.web

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.alp.app.databinding.FragmentWebBinding
import com.alp.app.ui.main.adapter.PlaygroundAdapter
import com.google.android.material.tabs.TabLayoutMediator

class WebFragment : Fragment(), HtmlFragment.OnButtonClickListener {

    private lateinit var contexto: Context
    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!
    private lateinit var playgroundAdapter: PlaygroundAdapter

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        binding.webView.apply {
            loadUrl("http://192.168.0.18/compiler-web/")
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }

        return binding.root
    }

    private fun setupUI() {
        playgroundAdapter = PlaygroundAdapter(fragmentManager = childFragmentManager, lifecycle = viewLifecycleOwner.lifecycle)
        binding.viewPager3.adapter = playgroundAdapter
        TabLayoutMediator(binding.tabLayout2, binding.viewPager3) {tab, position ->
            when(position){
                0 -> tab.text = "HTML"
                1 -> tab.text = "RESULTADO"
            }
        }.attach()
        binding.viewPager3.isUserInputEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

    override fun onChildFragClick(index: Int, data: String) {
        binding.viewPager3.currentItem = index
    }
}