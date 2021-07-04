/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/07/21 09:53 PM
 *
 */

/*
 * EHOALAELEALAE
 */

package com.alp.app.ui.main.view.fragments.languages.web

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alp.app.data.model.PlaygroundModel
import com.alp.app.databinding.FragmentWebBinding
import com.alp.app.ui.main.adapter.PlaygroundAdapter
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class WebFragment : Fragment() {

    private lateinit var contexto: Context
    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var playgroundAdapter: PlaygroundAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        val language = ArrayList<PlaygroundModel>()
        language.add(PlaygroundModel("HTML", "html", "php2"))
        language.add(PlaygroundModel("CSS", "csss","html2"))
        language.add(PlaygroundModel("JAVASCRIPT", "","html2"))
        val adapter = PlaygroundAdapter(contexto, language)
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout2, binding.viewPager2) {tab, position ->
            val texto = language[position]
            tab.text = texto.name
        }.attach()
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