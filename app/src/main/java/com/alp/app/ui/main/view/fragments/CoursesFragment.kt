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
import com.alp.app.data.model.CoursesModel
import com.alp.app.databinding.FragmentCoursesBinding
import com.alp.app.ui.main.adapter.CoursesAdapter
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
class CoursesFragment : Fragment() {

    private lateinit var contexto: Context
    private lateinit var id : String
    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    private var interstitial:InterstitialAd? = null
    private var count = 0
    private val dashboardViewModel: DashboardViewModel by viewModels()
    @Inject
    lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle!= null) {
            val name = bundle.getString("nombre", "no")
            val icon = bundle.getString("icono", "no")
            id = bundle.getString("id", "no")
            binding.tituloCategoria.text = name
            Glide.with(contexto).load(icon).into(binding.iconoCategoria)
        }
        binding.progress.visibility = View.VISIBLE
        setupUI()
        setupShowData()
        initAds()
        initLoadAds()
        initListeners()
        count += 1
        checkCounter()
        return binding.root
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

    private fun initLoadAds() {
        val adRequest = AdRequest.Builder().build()
        binding.banner.loadAd(adRequest)
    }


    private fun initAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireActivity(), "ca-app-pub-2689265379329623/8627761416", adRequest, object : InterstitialAdLoadCallback(){
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitial = interstitialAd
            }
            override fun onAdFailedToLoad(p0: LoadAdError) {
                interstitial = null
            }
        })
    }

    private fun checkCounter() {
        if(count == 3){
            showAds()
            count = 0
            initAds()
        }
    }

    private fun showAds(){
        interstitial?.show(requireActivity())
    }

    private fun setupUI() {
        with(binding){
            coursesAdapter  = CoursesAdapter(contexto)
            recycler.layoutManager = LinearLayoutManager(contexto)
            recycler.adapter = coursesAdapter
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getCourses(id).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: List<CoursesModel>) {
        coursesAdapter.apply {
            updateData(data)
            notifyDataSetChanged()
        }
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