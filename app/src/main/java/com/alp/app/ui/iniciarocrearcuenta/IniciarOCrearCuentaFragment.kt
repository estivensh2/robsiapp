package com.alp.app.ui.iniciarocrearcuenta

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.databinding.FragmentBienvenidaBinding
import com.alp.app.databinding.FragmentIniciarOCrearCuentaBinding

class IniciarOCrearCuentaFragment : Fragment() {

    private lateinit var viewModel: IniciarOCrearCuentaViewModel
    private var _binding : FragmentIniciarOCrearCuentaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIniciarOCrearCuentaBinding.inflate(layoutInflater, container, false)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (supportActionBar != null) supportActionBar.show()
        with(binding){
            botonIniciarSesion.setOnClickListener    { iniciarSesion()      }
            botonRegistrarCuenta.setOnClickListener  { crearCuenta()        }
            botonFacebook.setOnClickListener         { iniciarConFacebook() }
        }
        return binding.root
    }

    private fun iniciarConFacebook() {

    }

    private fun iniciarSesion() {
        findNavController().navigate(R.id.accion_iniciar_o_crear_a_iniciar_sesion)
    }

    private fun crearCuenta() {
        findNavController().navigate(R.id.accion_iniciar_o_crear_a_registrarme)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IniciarOCrearCuentaViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}