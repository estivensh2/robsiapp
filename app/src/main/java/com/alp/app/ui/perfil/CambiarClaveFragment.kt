package com.alp.app.ui.perfil

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.widget.EditText
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.RespuestaCambiarClave
import com.alp.app.databinding.FragmentCambiarClaveBinding
import com.alp.app.servicios.APIServicio
import com.alp.app.servicios.ClaseToast
import com.alp.app.servicios.ServicioBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CambiarClaveFragment : Fragment() {

    private lateinit var viewModel: CambiarClaveViewModel
    private var _binding: FragmentCambiarClaveBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCambiarClaveBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        with(binding){
            resultadoerror.visibility = View.GONE
            mostrarClave(mostrarClaveActual, claveAccesoActual)
            mostrarClave(mostrarClaveNueva, claveAccesoNueva)
            mostrarClave(mostrarClaveNuevaConfirmada, claveAccesoNuevaConfirmada)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CambiarClaveViewModel::class.java)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cambiar_clave, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.cambiar_clave)
        item.isVisible = false
        with(binding){
            claveAccesoNueva.onChange           { habilitarBoton(item) }
            claveAccesoActual.onChange          { habilitarBoton(item) }
            claveAccesoNuevaConfirmada.onChange { habilitarBoton(item) }
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun habilitarBoton(item: MenuItem) {
        with(binding){
            if (claveAccesoActual.length()>0){
                if (claveAccesoNueva.length()>0 && claveAccesoNuevaConfirmada.length()>0){
                    if (claveAccesoNueva.length()<6 && claveAccesoNuevaConfirmada.length()<6){
                        resultadoerror.visibility = View.VISIBLE
                        resultadoerror.text = resources.getString(R.string.texto_clave_minimo)
                    } else if (claveAccesoNueva.text.toString()!=claveAccesoNuevaConfirmada.text.toString()){
                        resultadoerror.visibility = View.VISIBLE
                        resultadoerror.text = resources.getString(R.string.texto_claves_no_coinciden)
                        item.isVisible = false
                    } else {
                        resultadoerror.visibility = View.GONE
                        item.isVisible = true
                    }
                } else {
                    resultadoerror.visibility = View.GONE
                    item.isVisible = false
                }
            } else {
                resultadoerror.visibility = View.GONE
               item.isVisible = false
            }
        }
    }

    private fun mostrarClave(boton: ToggleButton, campo: EditText){
        boton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                campo.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                campo.transformationMethod = PasswordTransformationMethod.getInstance()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cambiar_clave -> cambiarClave()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

    private fun cambiarClave() {
        binding.cargaContenido.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.cambiarClave(binding.claveAccesoActual.text.toString(), binding.claveAccesoNuevaConfirmada.text.toString(), "7").enqueue(object : Callback<RespuestaCambiarClave> {
                    override fun onResponse(call: Call<RespuestaCambiarClave>, response: Response<RespuestaCambiarClave>) {
                        val responsex = response.body()!!
                        activity?.runOnUiThread {
                            if (responsex.respuesta == "1") {
                                ClaseToast.mostrarx(contexto, getString(R.string.texto_clave_cambiada), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                                with(binding) {
                                    claveAccesoNueva.setText("")
                                    claveAccesoActual.setText("")
                                    claveAccesoNuevaConfirmada.setText("")
                                    cargaContenido.visibility = View.GONE
                                    findNavController().navigate(R.id.accion_cambiar_clave_a_perfil)
                                }
                            } else {
                                activity?.runOnUiThread {
                                   with(binding){
                                       cargaContenido.visibility = View.GONE
                                       resultadoerror.visibility = View.VISIBLE
                                       resultadoerror.text = resources.getString(R.string.texto_clave_incorrecta)
                                   }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<RespuestaCambiarClave>, t: Throwable) {
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
}