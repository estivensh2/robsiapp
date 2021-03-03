package com.alp.app.ui.iniciarsesion

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.RespuestaCrearCuentaData
import com.alp.app.data.RespuestaIniciarSesionData
import com.alp.app.data.RespuestaSliderData
import com.alp.app.data.RespuestaUsuarioData
import com.alp.app.databinding.FragmentIniciarSesionBinding
import com.alp.app.databinding.FragmentPerfilBinding
import com.alp.app.databinding.FragmentRegistrarUsuarioBinding
import com.alp.app.servicios.*
import com.alp.app.ui.bienvenida.BienvenidaFragmentDirections
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
    private var bundle = Bundle()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIniciarSesionBinding.inflate(layoutInflater, container, false)
        Preferencias.init(requireContext(), "preferenciasDeUsuario")
        with(binding){
            Glide.with(contexto).load(R.drawable.saludando).into(imagenIniciarSesion)
            botonIngresar.isEnabled = false
            botonIngresar.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
            botonIngresar.setOnClickListener { iniciarSesion() }
            correoElectronico.onChange       { habilitarBoton() }
            claveAcceso.onChange             { habilitarBoton() }
        }

        return binding.root
    }

    private fun iniciarSesion() {
        val correo = binding.correoElectronico.text.toString()
        val clave = binding.claveAcceso.text.toString()
        ProgressDialogo.mostrar(contexto)
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.iniciarSesion(correo, clave).enqueue(object : Callback<RespuestaIniciarSesionData> {
                    override fun onResponse(call: Call<RespuestaIniciarSesionData>, response: Response<RespuestaIniciarSesionData>) {
                        val response = response.body()!!
                        activity?.runOnUiThread {
                            if (response.respuesta.equals("1")) {
                                ProgressDialogo.ocultar()
                                Preferencias.escribir("id", response.datos.id)
                                Preferencias.escribir("sesionActiva", true)
                                val action = IniciarSesionFragmentDirections.accionIniciarANavegacionPrincipal(
                                    response.datos.nombres,
                                    response.datos.nombres,
                                    response.datos.apellidos,
                                    response.datos.notificaciones,
                                    response.datos.correo,
                                    response.datos.clave)
                                findNavController().navigate(action)
                            } else {
                                ProgressDialogo.ocultar()
                                ClaseToast.mostrarx(contexto, "Este correo ya existe", ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            }
                        }
                    }
                    override fun onFailure(call: Call<RespuestaIniciarSesionData>, t: Throwable) {
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IniciarSesionViewModel::class.java)

    }

    private fun habilitarBoton(){
        with(binding){
            if (correoElectronico.length()>0 && claveAcceso.length()>0 ){
                botonIngresar.isEnabled = true
                botonIngresar.setTextColor(ContextCompat.getColor(contexto, R.color.colorBlanco))
            } else if (!validarCorreo(correoElectronico.text.toString())){
                correoElectronico.error = "Correo no valido"
            } else {
                botonIngresar.isEnabled = false
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