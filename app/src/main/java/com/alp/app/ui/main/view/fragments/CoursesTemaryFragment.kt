package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.ui.main.adapter.CoursesTemaryAdapter
import com.alp.app.data.model.CoursesTemaryModel
import com.alp.app.databinding.FragmentInicioCursosDetalleBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoursesTemaryFragment : Fragment() {

    private lateinit var contexto: Context
    private lateinit var id : String
    private var _binding: FragmentInicioCursosDetalleBinding? = null
    private val binding get() = _binding!!
    private var interstitial:InterstitialAd? = null
    private var count = 0

    private val dashboardViewModel: DashboardViewModel by viewModels()
    @Inject
    lateinit var coursesTemaryAdapter: CoursesTemaryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInicioCursosDetalleBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            id = bundle.getString("id", "no")
            val nombre = bundle.getString("nombre", "no")
            val icono = bundle.getString("icono", "no")
            Glide.with(contexto).load(icono).into(binding.iconoCurso)
            binding.tituloCurso.text = nombre
        }
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
            coursesTemaryAdapter  = CoursesTemaryAdapter(contexto)
            recycler.layoutManager = LinearLayoutManager(contexto)
            recycler.adapter = coursesTemaryAdapter
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getCoursesTemary(id, PreferencesSingleton.leer("id","0").toString()).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.recycler.visibility = View.VISIBLE
                        showHideProgressBar(false)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        binding.recycler.visibility = View.VISIBLE
                        showMessage(response.message!!)
                        showHideProgressBar(false)
                    }
                    Status.LOADING -> {
                        binding.recycler.visibility = View.GONE
                        showHideProgressBar(true)
                    }
                }
            }
        })
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

    private fun renderList(data: List<CoursesTemaryModel>) {
        coursesTemaryAdapter.apply {
            updateData(data)
            notifyDataSetChanged()
        }
        val totalTemarioCompletado = data[0].total.toDouble()
        val totalCursosTemario = data.size.toDouble()
        val progreso = totalTemarioCompletado / totalCursosTemario * 100
        binding.progresoCurso.progress = progreso.toFloat()
        binding.progresoCurso.progressText = progreso.toInt().toString() + " %"
        when {
            progreso.toInt()==100 -> {
                binding.textoProgreso.text = resources.getString(R.string.texto_curso_completado)
            }
            progreso.toInt()==0 -> {
                binding.textoProgreso.text = resources.getString(R.string.texto_curso_sin_iniciar)
            }
            else -> {
                binding.textoProgreso.text = resources.getString(R.string.texto_curso_en_curso)
            }
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
        if(count == 5){
            showAds()
            count = 0
            initAds()
        }
    }

    private fun showAds(){
        interstitial?.show(requireActivity())
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