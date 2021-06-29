package com.alp.app.ui.main.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.alp.app.R
import com.alp.app.data.model.CategoryModel
import com.alp.app.data.model.SliderModel
import com.alp.app.databinding.FragmentHomeBinding
import com.alp.app.ui.main.adapter.CategoriesAdapter
import com.alp.app.ui.main.adapter.SliderAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Runnable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var runnable: Runnable
    private lateinit var contexto: Context
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val displayListaSlider = ArrayList<SliderModel>()
    private val handler = Handler(Looper.getMainLooper())
    private val handlerx = Handler(Looper.getMainLooper())
    private lateinit var runnablex: Runnable
    private val dashboardViewModel: DashboardViewModel by viewModels()
    @Inject
    lateinit var adapterCategories: CategoriesAdapter
    lateinit var adapterSlider: SliderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupUI()
        setupShowData()
        initLoadAds()
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            adapterCategories  = CategoriesAdapter(contexto)
            adapterSlider  = SliderAdapter(contexto)
            reciclador.layoutManager = LinearLayoutManager(contexto)
            reciclador.adapter = adapterCategories
            viewPager2.apply {
                adapter = adapterSlider
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        indicator.selection = position
                    }
                })
            }
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getCategories().observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.reciclador.visibility = View.VISIBLE
                        showHideProgressBar(false)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        binding.reciclador.visibility = View.VISIBLE
                        showMessage(response.message!!)
                        showHideProgressBar(false)
                    }
                    Status.LOADING -> {
                        binding.reciclador.visibility = View.GONE
                        showHideProgressBar(true)
                    }
                }
            }
        })
        dashboardViewModel.getSlider().observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.reciclador.visibility = View.VISIBLE
                        showHideProgressBar(false)
                        resource.data?.let { data -> retrieveSlider(data) }
                    }
                    Status.ERROR   -> {
                        binding.reciclador.visibility = View.VISIBLE
                        showMessage(response.message!!)
                        showHideProgressBar(false)
                    }
                    Status.LOADING -> {
                        binding.reciclador.visibility = View.GONE
                        showHideProgressBar(true)
                    }
                }
            }
        })
    }

    private fun retrieveSlider(lista: List<SliderModel>) {
        adapterSlider.apply {
            updateData(lista)
            notifyDataSetChanged()
        }
        binding.indicator.count = adapterSlider.list.size
    }


    private fun showHideProgressBar(showHide: Boolean){
        with(binding){
            if(showHide){
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.GONE
            }
        }
    }

    private fun showMessage(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(requireView(), message, duration).show()
    }

    private fun renderList(data: List<CategoryModel>) {
        adapterCategories.apply {
            updateData(data)
            notifyDataSetChanged()
        }
    }

    private fun initLoadAds() {
        val adRequest = AdRequest.Builder().build()
        binding.banner.loadAd(adRequest)
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
                        saludox.text = contexto.getString(R.string.text_good_afternoon)
                    } else {
                        saludox.text = contexto.getString(R.string.text_good_night)
                    }
                } else {
                    saludox.text = contexto.getString(R.string.text_good_morning)
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
        actualizarHora(binding.fechaActual,binding.bienvenido)
        ejecutarSlider(binding.viewPager2)
        super.onStart()
    }
}