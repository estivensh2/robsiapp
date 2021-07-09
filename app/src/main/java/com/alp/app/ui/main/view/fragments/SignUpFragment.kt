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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.SignUpModel
import com.alp.app.databinding.FragmentSignupBinding
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var contexto: Context
    private lateinit var functions: Functions
    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        functions = Functions(contexto)
        with(binding){
            functions.enabledButton(false, btnSignUp)
            btnSignUp.setOnClickListener { setupShowData() }
            iENames.onChange     { enabledButton() }
            iELastNames.onChange { enabledButton() }
            iEEmail.onChange     { enabledButton() }
            iEPassword.onChange  { enabledButton() }
        }
        return binding.root
    }

    private fun setupShowData() {
        val names = binding.iENames.text.toString()
        val lastNames = binding.iELastNames.text.toString()
        val email = binding.iEEmail.text.toString()
        val password = binding.iEPassword.text.toString()
        dashboardViewModel.setSignUp(names, lastNames, email, password).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderList(data: Response<SignUpModel>) {
        val response = data.body()!!
        if (response.response == 1) {
            findNavController().navigate(R.id.accion_registrar_a_iniciar_sesion)
        } else {
            with(binding){
                btnSignUp.text = resources.getString(R.string.text_register)
                DynamicToast.makeError(contexto, resources.getString(R.string.text_existing_email), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun enabledButton(){
        with(binding){
            if (iENames.length()>0 &&
                iELastNames.length()>0 &&
                iEEmail.length()>0 &&
                iEPassword.length()>0 && functions.validateEmail(iEEmail.text.toString())){
                functions.enabledButton(true, btnSignUp)
            } else {
                functions.enabledButton(false, btnSignUp)
            }
            if (!functions.validateEmail(iEEmail.text.toString())){
                functions.enabledButton(false, btnSignUp)
                iLEmail.error = resources.getString(R.string.text_invalid_email)
            } else {
                iLEmail.error = ""
            }
        }
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
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