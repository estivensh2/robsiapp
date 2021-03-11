package com.alp.app.ui.perfil

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
import com.alp.app.adaptadores.DiplomasAdaptador
import com.alp.app.data.RespuestaRecuperarDiploma
import com.alp.app.databinding.FragmentListadoDiplomadosBinding
import com.alp.app.servicios.APIServicio
import com.alp.app.servicios.ClaseToast
import com.alp.app.servicios.Preferencias
import com.alp.app.servicios.ServicioBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListadoDiplomadosFragment : Fragment() {

    private lateinit var viewModel: ListadoDiplomadosViewModel
    private var _binding: FragmentListadoDiplomadosBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val displayListaDiploma = ArrayList<RespuestaRecuperarDiploma>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListadoDiplomadosBinding.inflate(inflater, container, false)
        Preferencias.init(requireContext(), "preferenciasDeUsuario")
        binding.cargaContenido.visibility = View.VISIBLE
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListadoDiplomadosViewModel::class.java)
    }

    private fun recuperarDiploma() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.recuperarDiploma(Preferencias.leer("id","0").toString()).enqueue(object :
                    Callback<List<RespuestaRecuperarDiploma>> {
                    override fun onResponse(call: Call<List<RespuestaRecuperarDiploma>>, response: Response<List<RespuestaRecuperarDiploma>>) {
                        activity?.runOnUiThread {
                            if (response.body() == null) {
                                ClaseToast.mostrarx(contexto, getString(R.string.texto_sin_certificados), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            } else {
                                val adaptador = DiplomasAdaptador(displayListaDiploma, contexto)
                                adaptador.notifyDataSetChanged()
                                with(binding){
                                    adaptador.notifyDataSetChanged()
                                    displayListaDiploma.clear()
                                    displayListaDiploma.addAll(response.body()!!)
                                    recicladorDiplomas.layoutManager = LinearLayoutManager(contexto)
                                    recicladorDiplomas.setHasFixedSize(true)
                                    recicladorDiplomas.adapter = adaptador
                                }
                                binding.cargaContenido.visibility = View.GONE
                            }
                        }
                    }
                    override fun onFailure(call: Call<List<RespuestaRecuperarDiploma>>, t: Throwable) {
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
        recuperarDiploma()
        super.onStart()
    }

}