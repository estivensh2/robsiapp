package com.alp.app.ui.registrar

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.RespuestaCrearCuentaData
import com.alp.app.databinding.FragmentRegistrarUsuarioBinding
import com.alp.app.servicios.APIServicio
import com.alp.app.servicios.ClaseToast
import com.alp.app.servicios.ProgressDialogo
import com.alp.app.servicios.ServicioBuilder
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegistrarUsuarioFragment : Fragment() {

    private lateinit var viewModel: RegistrarUsuarioViewModel
    private lateinit var contexto: Context
    private var _binding : FragmentRegistrarUsuarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegistrarUsuarioBinding.inflate(layoutInflater, container, false)
        with(binding){
            Glide.with(contexto).load(R.drawable.saludando).into(imagenCrearCuenta)
            botonRegistrar.isEnabled = false
            botonRegistrar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
            botonRegistrar.setOnClickListener { insertarUsuario() }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrarUsuarioViewModel::class.java)
    }

    private fun insertarUsuario() {
        val nombres = binding.nombres.text.toString()
        val apellidos = binding.apellidos.text.toString()
        val correo = binding.correoElectronico.text.toString()
        val clave = binding.claveAcceso.text.toString()
        binding.animationView2.visibility = View.VISIBLE
        binding.botonRegistrar.text = ""
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.registrarUsuario(nombres, apellidos, correo, clave).enqueue(object : Callback<RespuestaCrearCuentaData> {
                    override fun onResponse(call: Call<RespuestaCrearCuentaData>, response: Response<RespuestaCrearCuentaData>) {
                        val responsex = response.body()!!
                        activity?.runOnUiThread {
                            if (responsex.respuestax == "1") {
                                ClaseToast.mostrarx(contexto, "Usuario registrado correctamente", ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                                findNavController().navigate(R.id.accion_registrar_a_iniciar_sesion)
                            } else {
                                with(binding){
                                    animationView2.visibility = View.GONE
                                    resultadoerror.visibility = View.VISIBLE
                                    botonRegistrar.text = resources.getString(R.string.texto_registrarme)
                                    resultadoerror.text = resources.getString(R.string.texto_correo_existente)
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<RespuestaCrearCuentaData>, t: Throwable) {
                        activity!!.runOnUiThread {
                            ClaseToast.mostrarx(contexto, getString(R.string.texto_error_conexion), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                        }
                    }
                })
            } catch (e: Throwable) {
                requireActivity().runOnUiThread {
                    ClaseToast.mostrarx(contexto, getString(R.string.texto_error_grave), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                }
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