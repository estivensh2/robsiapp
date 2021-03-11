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
import com.alp.app.adaptadores.CursosDetalleAdaptador
import com.alp.app.data.RespuestaCursosDetalleData
import com.alp.app.databinding.FragmentInicioCursosDetalleBinding
import com.alp.app.servicios.APIServicio
import com.alp.app.servicios.ClaseToast
import com.alp.app.servicios.Preferencias
import com.alp.app.servicios.ServicioBuilder
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InicioCursosDetalleFragment : Fragment() {

    private lateinit var viewModel: InicioCursosDetalleViewModel
    private lateinit var contexto: Context
    private lateinit var id : String
    private var _binding: FragmentInicioCursosDetalleBinding? = null
    private val binding get() = _binding!!
    private val displayListaCursosDetalle = ArrayList<RespuestaCursosDetalleData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInicioCursosDetalleBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            id = bundle.getString("id", "no")
            val nombre = bundle.getString("nombre", "no")
            val icono = bundle.getString("icono", "no")
            Glide.with(contexto).load(icono).into(binding.iconoCurso)
            binding.tituloCurso.text = nombre
        }
        binding.cargaContenido.visibility = View.VISIBLE
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InicioCursosDetalleViewModel::class.java)
    }

    private fun recuperarCursos() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.recuperarCursosDetalle(id, Preferencias.leer("id","0").toString()).enqueue(object :
                    Callback<List<RespuestaCursosDetalleData>> {
                    override fun onResponse(call: Call<List<RespuestaCursosDetalleData>>, response: Response<List<RespuestaCursosDetalleData>>) {
                        activity?.runOnUiThread {
                            if (response.body() == null) {
                                binding.cargaContenido.visibility = View.GONE
                                binding.cargarError.visibility = View.VISIBLE
                                binding.textoErrorSinTemario.text = resources.getString(R.string.texto_sin_temario)
                            } else {
                                val adaptador = CursosDetalleAdaptador(displayListaCursosDetalle, contexto)
                                val responsex = response.body()!!
                                adaptador.notifyDataSetChanged()
                                with(binding){
                                    val totalTemarioCompletado = responsex[0].total.toDouble()
                                    val totalCursosTemario = responsex.size.toDouble()
                                    val progreso = totalTemarioCompletado / totalCursosTemario * 100
                                    adaptador.notifyDataSetChanged()
                                    progresoCurso.progress = progreso.toFloat()
                                    progresoCurso.progressText = progreso.toInt().toString() + " %"
                                    displayListaCursosDetalle.clear()
                                    displayListaCursosDetalle.addAll(response.body()!!)
                                    reciclador.layoutManager = LinearLayoutManager(contexto)
                                    reciclador.setHasFixedSize(true)
                                    reciclador.adapter = adaptador
                                    when {
                                        progreso.toInt()==100 -> {
                                            textoProgreso.text = resources.getString(R.string.texto_curso_completado)
                                        }
                                        progreso.toInt()==0 -> {
                                            textoProgreso.text = resources.getString(R.string.texto_curso_sin_iniciar)
                                        }
                                        else -> {
                                            textoProgreso.text = resources.getString(R.string.texto_curso_en_curso)
                                        }
                                    }
                                }
                                binding.cargaContenido.visibility = View.GONE
                            }
                        }
                    }
                    override fun onFailure(call: Call<List<RespuestaCursosDetalleData>>, t: Throwable) {
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