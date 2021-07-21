/*
 * *
 *  * Created by estiv on 18/07/21, 12:38 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 18/07/21, 12:38 p. m.
 *
 */

package com.alp.app.ui.main.view.fragments.languages.web


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alp.app.R
import com.alp.app.databinding.FragmentHtmlBinding
import com.alp.app.singleton.PreferencesSingleton


class HtmlFragment : Fragment() {

    private lateinit var contexto: Context
    private var _binding: FragmentHtmlBinding? = null
    private val binding get() = _binding!!
    var onButtonClickListener: OnButtonClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHtmlBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        binding.btnRun.setOnClickListener {

        }
        return binding.root
    }

    interface OnButtonClickListener {
        fun onChildFragClick(index: Int, data: String)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onButtonClickListener = parentFragment as OnButtonClickListener?
        this.contexto = context
    }
}