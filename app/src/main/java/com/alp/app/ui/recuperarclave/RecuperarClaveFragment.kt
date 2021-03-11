package com.alp.app.ui.recuperarclave

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.RespuestaRecuperarClave
import com.alp.app.databinding.FragmentRecuperarClaveBinding
import com.alp.app.servicios.APIServicio
import com.alp.app.servicios.ClaseToast
import com.alp.app.servicios.ServicioBuilder
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RecuperarClaveFragment : Fragment() {
    private var _binding: FragmentRecuperarClaveBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RecuperarClaveViewModel
    private lateinit var contexto: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecuperarClaveBinding.inflate(layoutInflater, container, false)
        binding.botonRecuperarClave.isEnabled = false
        binding.botonRecuperarClave.setOnClickListener { recuperarClave() }
        binding.correoElectronico.onChange { habilitarBoton() }
        binding.botonRecuperarClave.setTextColor(ContextCompat.getColor(contexto, R.color.colorGrisClaroMedio))
        binding.resultadoerror.visibility = View.GONE
        Glide.with(contexto).load(R.drawable.saludando).into(binding.imagenRecuperarClave)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecuperarClaveViewModel::class.java)
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

    private fun recuperarClave(){
        val correo = binding.correoElectronico.text.toString()
        binding.cargaContenido.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.recuperarClave(correo).enqueue(object :
                    Callback<RespuestaRecuperarClave> {
                    override fun onResponse(call: Call<RespuestaRecuperarClave>, response: Response<RespuestaRecuperarClave>) {
                        val responsex = response.body()!!
                        activity?.runOnUiThread {
                            if (responsex.respuesta == "1") {
                                ClaseToast.mostrarx(contexto, getString(R.string.texto_correo_enviado), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                                binding.cargaContenido.visibility = View.GONE
                                findNavController().navigate(R.id.accion_recuperar_a_iniciar_o_crear)
                            } else {
                                binding.resultadoerror.visibility = View.VISIBLE
                                binding.resultadoerror.text = resources.getString(R.string.texto_corre_no_registrado)
                                binding.cargaContenido.visibility = View.GONE
                            }
                        }
                    }

                    override fun onFailure(call: Call<RespuestaRecuperarClave>, t: Throwable) {
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