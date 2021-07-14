/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 01:55 AM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.alp.app.R
import com.alp.app.ui.main.adapter.OnBoardingAdapter
import com.alp.app.data.model.InduccionData
import com.alp.app.databinding.FragmentOnboardingBinding
import com.alp.app.singleton.PreferencesSingleton

class OnBoardingFragment : Fragment() {

    private val arrayList = ArrayList<InduccionData>()
    private val displayList = ArrayList<InduccionData>()
    private var _binding : FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        arrayList.add(InduccionData(R.drawable.paso1, "Bienvenido", "En esta aplicacion podrás aprender las bases básicas para iniciar en la programación totalmente gratis."))
        arrayList.add(InduccionData(R.drawable.paso2, "Obten tu certificado", "Podrás obtener y descargar tu certificado de cada curso."))
        displayList.addAll(arrayList)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        val adapter = OnBoardingAdapter(requireContext(), displayList)
        binding.recicladorInduccion.adapter = adapter
        binding.indicator.count = displayList.size
        with(binding){
            recicladorInduccion.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    indicator.selection = position
                    if(position==1){
                        binding.saltar.visibility = View.INVISIBLE
                        binding.finalizar.visibility = View.VISIBLE
                    } else {
                        binding.saltar.visibility = View.VISIBLE
                        binding.finalizar.visibility = View.INVISIBLE
                    }
                }
            })
            finalizar.visibility = View.INVISIBLE
            saltar.setOnClickListener { skipOnBoarding() }
            finalizar.setOnClickListener {
                PreferencesSingleton.write("user_new", true)
                PreferencesSingleton.write("enabled_sound", true)
                findNavController().navigate(R.id.accion_induccion_a_iniciar_o_crear)
            }
        }
        supportActionBar?.hide()
        return binding.root
    }

    private fun skipOnBoarding() {
        PreferencesSingleton.write("user_new", true)
        PreferencesSingleton.write("enabled_sound", true)
        findNavController().navigate(R.id.accion_induccion_a_iniciar_o_crear)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}