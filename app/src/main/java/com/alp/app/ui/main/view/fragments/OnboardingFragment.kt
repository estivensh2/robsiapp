package com.alp.app.ui.main.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.ui.main.adapter.OnboardingAdapter
import com.alp.app.data.model.InduccionData
import com.alp.app.databinding.FragmentOnboardingBinding
import com.alp.app.singleton.PreferencesSingleton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingFragment : Fragment() {

    private val arrayList = ArrayList<InduccionData>()
    private val displayList = ArrayList<InduccionData>()
    private var _binding : FragmentOnboardingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        PreferencesSingleton.init(requireContext(), "preferenciasDeUsuario")
        arrayList.add(InduccionData(R.drawable.paso1, "Bienvenido", "En esta aplicacion podrás aprender las bases básicas para iniciar en la programación totalmente gratis."))
        arrayList.add(InduccionData(R.drawable.paso2, "Obten tu certificado", "Podrás obtener y descargar tu certificado de cada curso."))
        displayList.addAll(arrayList)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        val adaptador = OnboardingAdapter(requireContext(), displayList)
        binding.recicladorInduccion.adapter = adaptador
        TabLayoutMediator(binding.paginadorInduccion, binding.recicladorInduccion) { tab, _ ->
            tab.text = ""
        }.attach()
        with(binding){
            finalizar.visibility = View.INVISIBLE
            saltar.setOnClickListener { saltarInduccion() }
            paginadorInduccion.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    if (tab.position == 1){
                        binding.saltar.visibility = View.INVISIBLE
                        binding.finalizar.visibility = View.VISIBLE
                    } else {
                        binding.saltar.visibility = View.VISIBLE
                        binding.finalizar.visibility = View.INVISIBLE
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
            finalizar.setOnClickListener {
                PreferencesSingleton.escribir("nuevo", true)
                PreferencesSingleton.escribir("idsonidos", true)
                findNavController().navigate(R.id.accion_induccion_a_iniciar_o_crear)
            }
        }
        if (supportActionBar != null) supportActionBar.hide()
        return binding.root
    }

    private fun saltarInduccion() {
        PreferencesSingleton.escribir("nuevo", true)
        PreferencesSingleton.escribir("idsonidos", true)
        findNavController().navigate(R.id.accion_induccion_a_iniciar_o_crear)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}