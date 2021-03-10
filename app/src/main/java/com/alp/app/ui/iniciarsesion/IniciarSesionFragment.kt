package com.alp.app.ui.iniciarsesion

import android.content.Context
import androidx.lifecycle.ViewModelProvider
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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.RespuestaIniciarSesionData
import com.alp.app.databinding.FragmentIniciarSesionBinding
import com.alp.app.servicios.*
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class IniciarSesionFragment : Fragment() {


    private lateinit var viewModel: IniciarSesionViewModel
    private var _binding: FragmentIniciarSesionBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentIniciarSesionBinding.inflate(layoutInflater, container, false)
        Preferencias.init(requireContext(), "preferenciasDeUsuario")
        with(binding){
            Glide.with(contexto).load(R.drawable.saludando).into(imagenIniciarSesion)
            botonIngresar.isEnabled = false
            botonIngresar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
            botonIngresar.setOnClickListener { iniciarSesion() }
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

    private fun iniciarSesion() {
        val correo = binding.correoElectronico.text.toString()
        val clave = binding.claveAcceso.text.toString()
        //binding.cargaContenido.visibility = View.VISIBLE
        binding.animationView2.visibility = View.VISIBLE
        binding.botonIngresar.text = ""
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.iniciarSesion(correo, clave).enqueue(object : Callback<RespuestaIniciarSesionData> {
                    override fun onResponse(call: Call<RespuestaIniciarSesionData>, response: Response<RespuestaIniciarSesionData>) {
                        val responsex = response.body()!!
                        activity?.runOnUiThread {
                            if (responsex.respuesta == "1") {
                                Preferencias.escribir("id", responsex.datos.id)
                                Preferencias.escribir("sesionActiva", true)
                                findNavController().navigate(R.id.accion_iniciar_a_navegacion_principal)
                                activity?.finish()
                                binding.animationView2.visibility = View.GONE
                            } else {
                                with(binding){
                                    animationView2.visibility = View.GONE
                                    resultadoerror.visibility = View.VISIBLE
                                    botonIngresar.text = resources.getString(R.string.texto_ingresar)
                                    resultadoerror.text = resources.getString(R.string.texto_datos_incorrectos)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<RespuestaIniciarSesionData>, t: Throwable) {
                        activity!!.runOnUiThread {
                            ClaseToast.mostrarx(contexto, getString(R.string.texto_error_conexion), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            binding.cargaContenido.visibility = View.VISIBLE
                        }
                    }
                })
            } catch (e: Throwable) {
                requireActivity().runOnUiThread {
                    ClaseToast.mostrarx(contexto, getString(R.string.texto_error_grave), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                    binding.cargaContenido.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IniciarSesionViewModel::class.java)

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