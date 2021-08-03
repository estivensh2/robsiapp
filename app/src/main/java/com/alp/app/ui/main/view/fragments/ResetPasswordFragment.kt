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
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.ResetPasswordModel
import com.alp.app.databinding.FragmentResetPasswordBinding
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.material.textfield.TextInputEditText
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetPasswordBinding? = null
    private lateinit var functions : Functions
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        functions = Functions(contexto)
        with(binding){
            functions.enabledButton(false, btnResetPassword)
            btnResetPassword.setOnClickListener { setupShowData() }
            iEEmail.onChange { enabledButton() }
        }
        return binding.root
    }

    private fun setupShowData() {
        val email = binding.iEEmail.text.toString()
        dashboardViewModel.setResetPassword(email).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun renderList(data: Response<ResetPasswordModel>) {
        val response = data.body()!!
        if (response.response == "1") {
            DynamicToast.makeSuccess(contexto, getString(R.string.text_sent_email), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.accion_recuperar_a_iniciar_o_crear)
        } else {
            binding.iLEmail.error = resources.getString(R.string.text_unregistered_email)
        }
    }

    private fun enabledButton(){
        with(binding){
            if (iEEmail.length()>0){
                functions.enabledButton(true, btnResetPassword)
            } else {
                functions.enabledButton(false, btnResetPassword)
            }
            if (!functions.validateEmail(iEEmail.text.toString())){
                binding.iLEmail.error = resources.getString(R.string.text_invalid_email)
                functions.enabledButton(false, btnResetPassword)
            } else {
                binding.iLEmail.error = ""
            }
        }
    }

    private fun TextInputEditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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