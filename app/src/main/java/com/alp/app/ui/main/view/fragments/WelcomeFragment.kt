package com.alp.app.ui.main.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.databinding.FragmentWelcomeBinding
import com.alp.app.singleton.PreferencesSingleton
import com.bumptech.glide.Glide

class WelcomeFragment : Fragment() {

    private var _binding : FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val tiempo: Long = 2000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.hide()
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
        if (PreferencesSingleton.read("user_new", false) == true) {
            if (PreferencesSingleton.read("active_session", false) == true){
                lifecycleScope.launchWhenResumed {
                    findNavController().navigate(R.id.action_navegacion_bienvenida_to_principalActivity2)
                }
                activity?.finish()
            } else {
                findNavController().navigate(R.id.accion_bienvenida_a_iniciar_o_crear_cuenta)
            }
        } else {
            findNavController().navigate(R.id.accion_bienvenida_a_induccion)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}