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
            iENames.onChange     { habilitarBoton() }
            iELastNames.onChange { habilitarBoton() }
            iEEmail.onChange     { habilitarBoton() }
            iEPassword.onChange  { habilitarBoton() }
        }
        return binding.root
    }

    private fun setupShowData() {
        val nombres = binding.iENames.text.toString()
        val apellidos = binding.iELastNames.text.toString()
        val correo = binding.iEEmail.text.toString()
        val clave = binding.iEPassword.text.toString()
        dashboardViewModel.setSignUp(nombres, apellidos, correo, clave).observe(requireActivity(), Observer { response ->
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
        if (response.data == "1") {
            DynamicToast.makeSuccess(contexto, "usuario registrado correctamente", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.accion_registrar_a_iniciar_sesion)
        } else {
            with(binding){
                btnSignUp.text = resources.getString(R.string.texto_registrarme)
                DynamicToast.makeError(contexto, resources.getString(R.string.texto_correo_existente), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun habilitarBoton(){
        with(binding){
            if (iENames.length()>0 &&
                iELastNames.length()>0 &&
                iEEmail.length()>0 &&
                iEPassword.length()>0 && functions.validarCorreo(iEEmail.text.toString())){ // validamos que los campos sean mayor a vacio y correo sea valido para habilitar el boton
                functions.enabledButton(true, btnSignUp)
            } else {
                functions.enabledButton(false, btnSignUp)
            }
            if (!functions.validarCorreo(iEEmail.text.toString())){
                functions.enabledButton(false, btnSignUp)
                iLEmail.error = resources.getString(R.string.texto_correo_invalido)
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