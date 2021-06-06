package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.SignUpModel
import com.alp.app.databinding.FragmentSignupBinding
import com.alp.app.singleton.ClaseToast
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import java.util.regex.Pattern


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var contexto: Context
    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        with(binding){
            Glide.with(contexto).load(R.drawable.saludando).into(imagenCrearCuenta)
            botonRegistrar.isEnabled = false
            botonRegistrar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
            botonRegistrar.setOnClickListener { setupShowData() }
            nombres.onChange { habilitarBoton() }
            apellidos.onChange { habilitarBoton() }
            correoElectronico.onChange { habilitarBoton() }
            claveAcceso.onChange { habilitarBoton() }
            mostrarClaveActual.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) claveAcceso.transformationMethod = HideReturnsTransformationMethod.getInstance()
                else claveAcceso.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        return binding.root
    }

    private fun setupShowData() {
        val nombres = binding.nombres.text.toString()
        val apellidos = binding.apellidos.text.toString()
        val correo = binding.correoElectronico.text.toString()
        val clave = binding.claveAcceso.text.toString()
        dashboardViewModel.setSignUp(nombres, apellidos, correo, clave).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: Response<SignUpModel>) {
        val response = data.body()!!
        if (response.respuestax == "1") {
            ClaseToast.mostrarx(contexto, "Usuario registrado correctamente", ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
            findNavController().navigate(R.id.accion_registrar_a_iniciar_sesion)
        } else {
            with(binding){
                resultadoerror.visibility = View.VISIBLE
                botonRegistrar.text = resources.getString(R.string.texto_registrarme)
                resultadoerror.text = resources.getString(R.string.texto_correo_existente)
            }
        }
    }

    private fun habilitarBoton(){
        with(binding){
            if (nombres.length()>0 &&
                apellidos.length()>0 &&
                correoElectronico.length()>0 &&
                claveAcceso.length()>0 && validarCorreo(correoElectronico.text.toString())){ // validamos que los campos sean mayor a vacio y correo sea valido para habilitar el boton
                botonRegistrar.isEnabled = true
                botonRegistrar.setTextColor(ContextCompat.getColor(contexto, R.color.colorAmarilloClaro))
            } else {
                botonRegistrar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
                botonRegistrar.isEnabled = false
            }
            if (!validarCorreo(correoElectronico.text.toString())){
                botonRegistrar.isEnabled = false
                resultadoerror.visibility = View.VISIBLE
                resultadoerror.text = resources.getString(R.string.texto_correo_invalido)
                botonRegistrar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
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