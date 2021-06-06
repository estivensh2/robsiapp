package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.ResetPasswordModel
import com.alp.app.databinding.FragmentRecuperarClaveBinding
import com.alp.app.singleton.ClaseToast
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import java.util.regex.Pattern

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentRecuperarClaveBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecuperarClaveBinding.inflate(layoutInflater, container, false)
        binding.botonRecuperarClave.isEnabled = false
        binding.botonRecuperarClave.setOnClickListener { setupShowData() }
        binding.correoElectronico.onChange { habilitarBoton() }
        binding.botonRecuperarClave.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
        binding.resultadoerror.visibility = View.GONE
        Glide.with(contexto).load(R.drawable.saludando).into(binding.imagenRecuperarClave)
        return binding.root
    }

    private fun setupShowData() {
        val correo = binding.correoElectronico.text.toString()
        dashboardViewModel.setResetPassword(correo).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        showHideProgressBar(false)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        showMessage(response.message!!)
                        showHideProgressBar(false)
                    }
                    Status.LOADING -> {
                        showHideProgressBar(true)
                    }
                }
            }
        })
    }

    private fun showHideProgressBar(showHide: Boolean){
        with(binding){
            if(showHide){
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.GONE
            }
        }
    }

    private fun showMessage(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(requireView(), message, duration).show()
    }

    private fun renderList(data: Response<ResetPasswordModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            ClaseToast.mostrarx(contexto, getString(R.string.texto_correo_enviado), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
            findNavController().navigate(R.id.accion_recuperar_a_iniciar_o_crear)
        } else {
            binding.resultadoerror.visibility = View.VISIBLE
            binding.resultadoerror.text = resources.getString(R.string.texto_corre_no_registrado)
        }
    }

    private fun habilitarBoton(){
        with(binding){
            if (correoElectronico.length()>0){
                botonRecuperarClave.isEnabled = true
                botonRecuperarClave.setTextColor(ContextCompat.getColor(contexto, R.color.colorAmarilloClaro))
            } else {
                botonRecuperarClave.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
                botonRecuperarClave.isEnabled = false
            }
            if (!validarCorreo(correoElectronico.text.toString())){
                correoElectronico.error =  resources.getString(R.string.texto_correo_invalido)
                botonRecuperarClave.isEnabled = false
                botonRecuperarClave.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
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

    private fun validarCorreo(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
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