package com.alp.app.ui.main.views.view.fragments

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
import com.alp.app.ui.main.views.adapter.CategoriesAdapter
import com.alp.app.ui.main.views.adapter.SliderAdapter
import com.alp.app.ui.main.views.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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
    private var interstitial:InterstitialAd? = null
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var count = 0

    @Inject
    lateinit var adapterCategories: CategoriesAdapter
    lateinit var adapterSlider: SliderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupUI()
        setupShowData()

        initAds()
        initListeners()
        count += 1
        checkCounter()
        binding.progress.visibility = View.VISIBLE
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

    private fun initListeners() {
        interstitial?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
            }
            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            }
            override fun onAdShowedFullScreenContent() {
                interstitial = null
            }
        }
    }
    private fun initAds() {
        val adRequest = AdRequest
            .Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("9E03F6B2BD01C42FCB0C36D6D2AA7767")
            .build()
        InterstitialAd.load(requireActivity(), "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback(){
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitial = interstitialAd
            }
            override fun onAdFailedToLoad(p0: LoadAdError) {
                interstitial = null
            }
        })
    }
    private fun checkCounter() {
        if(count == 4){
            showAds()
            count = 0
            initAds()
        }
    }

    private fun showAds(){
        interstitial?.show(requireActivity())
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
        actualizarHora(binding.fechaActual,binding.bienvenido)
        ejecutarSlider(binding.viewPager2)
        super.onStart()
    }
}