package com.alp.app.ui.main.views.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}