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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alp.app.R
import com.alp.app.data.model.DetailTopicModel
import com.alp.app.databinding.FragmentOutputBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.view.fragments.*

private const val ARG_PARAM1 = "param1"

class OutputFragment : Fragment() {
    private lateinit var contexto: Context
    private var _binding: FragmentOutputBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOutputBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        Toast.makeText(contexto, "$param1", Toast.LENGTH_SHORT).show()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

    companion object {
        @JvmStatic fun newInstance(data: String) = OutputFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, data)
            }
        }
    }
}