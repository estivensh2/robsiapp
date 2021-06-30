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
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.SigninModel
import com.alp.app.databinding.FragmentSigninBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.material.textfield.TextInputEditText
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        functions = Functions(contexto)
        with(binding){
            functions.enabledButton(false, binding.btnSignIn)
            btnSignIn.setOnClickListener { setupShowData() }
            iEEmail.onChange     { enabledButton() }
            iEPassword.onChange  { enabledButton() }
            btnResetPassword.setOnClickListener { v -> Navigation.findNavController(v).navigate(R.id.accion_iniciar_sesion_a_recuperar_clave) }
        }
        return binding.root
    }

    private fun setupShowData() {
        val email    = binding.iEEmail.text.toString()
        val password = binding.iEPassword.text.toString()
        dashboardViewModel.setSignIn(email, password).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        functions.showHideProgressBar(false, binding.progress)
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderList(data: Response<SigninModel>) {
        val response = data.body()!!
        if (response.data == "1") {
            PreferencesSingleton.write("id_user" , response.id_user)
            PreferencesSingleton.write("active_session", true)
            findNavController().navigate(R.id.accion_iniciar_a_navegacion_principal)
            activity?.finish()
        } else {
            with(binding){
                btnSignIn.text = resources.getString(R.string.text_sign_in)
                DynamicToast.makeError(contexto, resources.getString(R.string.text_incorrect_data), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun enabledButton(){
        with(binding){
            if (iEEmail.length()>0 && iEPassword.length()>0 ){
                functions.enabledButton(true, btnSignIn)
            } else {
                functions.enabledButton(false, btnSignIn)
            }
            if (!functions.validateEmail(iEEmail.text.toString())){
                iLEmail.error = resources.getString(R.string.text_invalid_email)
                functions.enabledButton(false, btnSignIn)
            } else {
                iLEmail.error = ""
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