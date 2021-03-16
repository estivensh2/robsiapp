package com.alp.app.ui.inicio

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.adaptadores.CategoriasAdaptador
import com.alp.app.data.RespuestaCategoriaData
import com.alp.app.data.RespuestaExamen
import com.alp.app.databinding.FragmentInicioCursosBinding
import com.alp.app.databinding.FragmentInicioCursosDetalleTemarioExamenBinding
import com.alp.app.servicios.APIServicio
import com.alp.app.servicios.ClaseToast
import com.alp.app.servicios.ServicioBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.String


class InicioCursosDetalleTemarioExamenFragment : Fragment() {


    private lateinit var viewModel: InicioCursosDetalleTemarioExamenViewModel
    private var _binding: FragmentInicioCursosDetalleTemarioExamenBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioCursosDetalleTemarioExamenBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle!= null) {
            val idcursox = bundle.getString("idcursox", "no")
            Toast.makeText(contexto, idcursox, Toast.LENGTH_SHORT).show()
        }
        val route = arrayOf("PYTHON","HTML", "CSS", "JAVASCRIPT")
        for(i in route) {
            val radioButton = RadioButton(contexto)
            radioButton.setText(i)
            binding.radio.addView(radioButton)
        }
        binding.validar.setOnClickListener { 
            val obtener = binding.radio.checkedRadioButtonId
            Toast.makeText(contexto, "$obtener", Toast.LENGTH_SHORT).show()
            if (obtener==3) {
                Toast.makeText(contexto, "si", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(contexto, "error", Toast.LENGTH_SHORT).show()
            }

        }
        recuperarExamenes()
        return binding.root
    }

    private fun recuperarExamenes() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.recuperarExamen().enqueue(object :
                        Callback<List<RespuestaExamen>> {
                    override fun onResponse(call: Call<List<RespuestaExamen>>, response: Response<List<RespuestaExamen>>) {
                        activity?.runOnUiThread {
                            val x = response.body()!!
                            Log.d("resultado", "${x.size} ola")
                        }
                    }
                    override fun onFailure(call: Call<List<RespuestaExamen>>, t: Throwable) {
                        Log.d("resultado", "${t} ola1")
                    }
                })
            } catch (e: Throwable) {
                Log.d("resultado", "${e} ola2")
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(InicioCursosDetalleTemarioExamenViewModel::class.java)
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