package com.alp.app.ui.inicio

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.adaptadores.CursosAdaptador
import com.alp.app.data.RespuestaCursosData
import com.alp.app.databinding.FragmentInicioCursosBinding
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

class InicioCursosFragment : Fragment() {

    private lateinit var contexto: Context
    private lateinit var viewModel: InicioCursosViewModel
    private lateinit var id : String
    private var _binding: FragmentInicioCursosBinding? = null
    private val binding get() = _binding!!
    private val displayListaCursos = ArrayList<RespuestaCursosData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInicioCursosBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle!= null) {
            val nombre = bundle.getString("nombre", "no")
            val icono = bundle.getString("icono", "no")
            id = bundle.getString("id", "no")
            binding.tituloCategoria.text = nombre
            Glide.with(contexto).load(icono).into(binding.iconoCategoria)
        }
        binding.cargaContenido.visibility = View.VISIBLE
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InicioCursosViewModel::class.java)
    }

    private fun recuperarCursos() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.recuperarCursos(id).enqueue(object :
                    Callback<List<RespuestaCursosData>> {
                    override fun onResponse(call: Call<List<RespuestaCursosData>>, response: Response<List<RespuestaCursosData>>) {
                        activity?.runOnUiThread {
                            if (response.body() == null){
                                ClaseToast.mostrarx(contexto, getString(R.string.texto_sin_categorias), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            } else {
                                val adaptador = CursosAdaptador(displayListaCursos, contexto)
                                adaptador.notifyDataSetChanged()
                                with(binding){
                                    cargaContenido.visibility = View.GONE
                                    displayListaCursos.clear()
                                    displayListaCursos.addAll(response.body()!!)
                                    recicladorCursos.layoutManager = LinearLayoutManager(contexto)
                                    recicladorCursos.setHasFixedSize(true)
                                    recicladorCursos.adapter = adaptador
                                    cargaContenido.visibility = View.GONE
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<List<RespuestaCursosData>>, t: Throwable) {
                        activity!!.runOnUiThread {
                            ClaseToast.mostrarx(contexto, getString(R.string.texto_error_conexion), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            binding.cargaContenido.visibility = View.GONE
                        }
                    }
                })
            } catch (e: Throwable) {
                e.printStackTrace()
                ClaseToast.mostrarx(contexto, getString(R.string.texto_error_grave), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                binding.cargaContenido.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

    override fun onStart() {
        recuperarCursos()
        super.onStart()
    }
}