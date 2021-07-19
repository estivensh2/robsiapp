/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/07/21 05:31 PM
 *
 */

/*
 * EHOALAELEALAE
 */

package com.alp.app.ui.main.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.databinding.FragmentCodeBinding
import com.alp.app.ui.main.adapter.PlaygroundAdapter
import com.alp.app.ui.main.adapter.ReviewAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

@SuppressLint("SetJavaScriptEnabled")
class CodeFragment : Fragment(){

    private lateinit var contexto: Context
    private var _binding: FragmentCodeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, avedInstanceState: Bundle?): View {
        _binding = FragmentCodeBinding.inflate(inflater, container, false)
        binding.floatingActionButton.setOnClickListener { showSimpleAdapterAlertDialog() }
        return binding.root
    }

    private fun showSimpleAdapterAlertDialog() {
        val items = arrayOf("Web (Beta)")
        val dialog = MaterialAlertDialogBuilder(contexto)
        dialog.setTitle(resources.getString(R.string.title_languages))
        dialog.setItems(items) { _, which ->
            when(which){
                0 -> findNavController().navigate(R.id.action_codeFragment_to_webFragment)
            }
        }
        dialog.show()
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