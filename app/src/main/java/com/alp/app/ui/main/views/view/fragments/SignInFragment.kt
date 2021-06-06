package com.alp.app.ui.main.views.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.ProfileModel
import com.alp.app.data.model.SigninModel
import com.alp.app.databinding.FragmentSigninBinding
import com.alp.app.servicios.*
import com.alp.app.ui.main.views.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
        PreferencesSingleton.init(requireContext(), "preferenciasDeUsuario")
        with(binding){
            Glide.with(contexto).load(R.drawable.saludando).into(imagenIniciarSesion)
            botonIngresar.isEnabled = false
            botonIngresar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
            botonIngresar.setOnClickListener { setupShowData() }
            correoElectronico.onChange       { habilitarBoton() }
            claveAcceso.onChange             { habilitarBoton() }
            mostrarClaveActual.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) claveAcceso.transformationMethod = HideReturnsTransformationMethod.getInstance()
                else claveAcceso.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            botonRecuperarClave.setOnClickListener { v -> Navigation.findNavController(v).navigate(R.id.accion_iniciar_sesion_a_recuperar_clave) }
        }

        return binding.root
    }

    private fun setupShowData() {
        val correo = binding.correoElectronico.text.toString()
        val clave = binding.claveAcceso.text.toString()
        dashboardViewModel.setSignIn(correo, clave).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: Response<SigninModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            PreferencesSingleton.escribir("id", response.id)
            PreferencesSingleton.escribir("sesionActiva", true)
            findNavController().navigate(R.id.accion_iniciar_a_navegacion_principal)
            activity?.finish()
        } else {
            with(binding){
                resultadoerror.visibility = View.VISIBLE
                botonIngresar.text = resources.getString(R.string.texto_ingresar)
                resultadoerror.text = resources.getString(R.string.texto_datos_incorrectos)
            }
        }
    }

    private fun habilitarBoton(){
        with(binding){
            if (correoElectronico.length()>0 && claveAcceso.length()>0 ){
                botonIngresar.isEnabled = true
                botonIngresar.setTextColor(ContextCompat.getColor(contexto, R.color.colorAmarilloClaro))
            } else {
                botonIngresar.isEnabled = false
                botonIngresar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
            }
            if (!validarCorreo(correoElectronico.text.toString())){
                botonIngresar.isEnabled = false
                resultadoerror.visibility = View.VISIBLE
                resultadoerror.text = resources.getString(R.string.texto_correo_invalido)
                botonIngresar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
            } else {
                resultadoerror.visibility = View.GONE
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