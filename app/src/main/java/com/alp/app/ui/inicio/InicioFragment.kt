package com.alp.app.ui.inicio

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.alp.app.R
import com.alp.app.adaptadores.CategoriasAdaptador
import com.alp.app.adaptadores.SliderAdaptador
import com.alp.app.data.RespuestaCategoriaData
import com.alp.app.data.RespuestaSliderData
import com.alp.app.databinding.FragmentInicioBinding
import com.alp.app.servicios.*
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Runnable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InicioFragment : Fragment() {

    private lateinit var inicioViewModel: InicioViewModel
    private lateinit var runnable: Runnable
    private lateinit var contexto: Context
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!
    private val displayListaSlider = ArrayList<RespuestaSliderData>()
    private val displayListaCategorias = ArrayList<RespuestaCategoriaData>()
    private val handler = Handler()
    private val handlerx = Handler()
    private lateinit var runnablex: Runnable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        inicioViewModel = ViewModelProvider(this).get(InicioViewModel::class.java)
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        inicioViewModel.text.observe(viewLifecycleOwner, {
        })
        binding.cargaContenido.visibility = View.VISIBLE
        return binding.root
    }

    private fun recuperarSlider() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.recuperarSliderx().enqueue(object :
                    Callback<List<RespuestaSliderData>> {
                    override fun onResponse(call: Call<List<RespuestaSliderData>>, response: Response<List<RespuestaSliderData>>) {
                        activity?.runOnUiThread {
                            with(binding){
                                cargaContenido.visibility = View.GONE
                                displayListaSlider.clear()
                                displayListaSlider.addAll(response.body()!!)
                                paginadorx.adapter = SliderAdaptador(displayListaSlider, contexto)
                                TabLayoutMediator(puntosTab, paginadorx) { tab, _ ->
                                    tab.icon = ContextCompat.getDrawable(contexto, R.drawable.slider_puntos)
                                }.attach()
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<RespuestaSliderData>>, t: Throwable) {
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

    private fun recuperarCategorias() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.recuperarCategorias().enqueue(object :
                    Callback<List<RespuestaCategoriaData>> {
                    override fun onResponse(call: Call<List<RespuestaCategoriaData>>, response: Response<List<RespuestaCategoriaData>>) {
                        activity?.runOnUiThread {
                            val adaptador = CategoriasAdaptador(displayListaCategorias, contexto)
                            adaptador.notifyDataSetChanged()
                            displayListaCategorias.clear()
                            displayListaCategorias.addAll(response.body()!!)
                            binding.reciclador.layoutManager = LinearLayoutManager(contexto)
                            binding.reciclador.setHasFixedSize(true)
                            binding.reciclador.adapter = adaptador
                            binding.cargaContenido.visibility = View.GONE
                        }
                    }
                    override fun onFailure(call: Call<List<RespuestaCategoriaData>>, t: Throwable) {
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

    private fun ejecutarSlider(paginador: ViewPager2){
        runnable = object : Runnable {
            override fun run() {
                var contador = paginador.currentItem
                if (contador == paginador.currentItem) contador++
                if (contador == displayListaSlider.size) contador = 0
                paginador.setCurrentItem(contador, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)
    }

    private fun actualizarHora(fecha: TextView, saludox:TextView) {
        runnablex = object: Runnable {
            override fun run() {
                fecha.text = fechaActual("hh:mm a")
                val saludo = fechaActual("a")
                val hora = fechaActual("h")
                if (saludo == "p. m."){
                    if (hora.toInt()<6){
                        saludox.text = contexto.getString(R.string.texto_buenas_tardes)
                    } else {
                        saludox.text = contexto.getString(R.string.texto_buenas_noches)
                    }
                } else {
                    saludox.text = contexto.getString(R.string.texto_buenos_dias)
                }
                handler.postDelayed(this, 1000)
            }
        }
        handlerx.post(runnablex)
    }

    
    @SuppressLint("SimpleDateFormat")
    private fun fechaActual(patron: String): String {
        val simpleDateFormat = SimpleDateFormat(patron)
        return simpleDateFormat.format(Date())
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        handlerx.removeCallbacks(runnablex)
        super.onPause()
    }

    override fun onResume() {
        handlerx.postDelayed(runnablex,1000) // reloj
        super.onResume()
    }

    override fun onStart() {
        recuperarSlider()
        recuperarCategorias()
        actualizarHora(binding.fechaActual,binding.bienvenido)
        ejecutarSlider(binding.paginadorx)
        super.onStart()
    }
}