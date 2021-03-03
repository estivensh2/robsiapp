package com.alp.app.ui.registrar

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
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
        ProgressDialogo.mostrar(contexto)
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.registrarUsuario(nombres, apellidos, correo, clave).enqueue(object : Callback<RespuestaCrearCuentaData> {
                    override fun onResponse(call: Call<RespuestaCrearCuentaData>, response: Response<RespuestaCrearCuentaData>) {
                        val response = response.body()!!
                        activity?.runOnUiThread {
                            if (response.respuestax.equals("1")) {
                                ProgressDialogo.ocultar()
                                ClaseToast.mostrarx(contexto, "Usuario registrado correctamente", ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                                findNavController().navigate(R.id.accion_registrar_a_iniciar_sesion)
                            } else {
                                ProgressDialogo.ocultar()
                                ClaseToast.mostrarx(contexto, "Este correo ya existe", ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            }
                        }
                    }
                    override fun onFailure(call: Call<RespuestaCrearCuentaData>, t: Throwable) {
                        activity!!.runOnUiThread {
                            Log.d("error", t.toString())
                        }
                    }
                })
            } catch (e: Throwable) {
                e.printStackTrace()
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
                botonRegistrar.setTextColor(ContextCompat.getColor(contexto, R.color.colorBlanco))
            } else {
                correoElectronico.error = "Correo no valido"
                botonRegistrar.isEnabled = false
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