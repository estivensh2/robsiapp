package com.alp.app.ui.main.views.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.databinding.FragmentWelcomeBinding
import com.alp.app.servicios.PreferencesSingleton
import com.bumptech.glide.Glide

class WelcomeFragment : Fragment() {

    private var _binding : FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val tiempo: Long = 2000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        PreferencesSingleton.init(requireContext(), "preferenciasDeUsuario")
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (supportActionBar != null) supportActionBar.hide()
        crearAnimacion()
        return binding.root
    }

    private fun crearAnimacion() {
        val url = ContextCompat.getDrawable(requireContext(), R.drawable.logobienvenida)
        Glide.with(this).load(url).into(binding.logoanimacion)
        Handler(Looper.getMainLooper()).postDelayed({
            validarInicioPrimeraVezYEstadoSesion()
        }, tiempo)
    }

    private fun validarInicioPrimeraVezYEstadoSesion() {
        if (PreferencesSingleton.leer("nuevo", false) == true) { // aca verificamos si el usuario ya inicio la app por primera vez
            if (PreferencesSingleton.leer("sesionActiva", false) == true){ // aca verificamos el estado de la sesion
                findNavController().navigate(R.id.accion_bienvenida_a_inicio)
                activity?.finish()
            } else { // si no existe lo redirigimos al iniciar sesion o crear cuenta
                findNavController().navigate(R.id.accion_bienvenida_a_iniciar_o_crear_cuenta)
            }
        } else { // si no ha iniciado la app lo redirimos a la induccion
            findNavController().navigate(R.id.accion_bienvenida_a_induccion)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}