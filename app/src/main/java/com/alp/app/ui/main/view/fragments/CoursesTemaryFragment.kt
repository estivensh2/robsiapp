package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.ui.main.adapter.CoursesTemaryAdapter
import com.alp.app.data.model.CoursesTemaryModel
import com.alp.app.databinding.FragmentCoursesTemaryBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoursesTemaryFragment : Fragment() {

    private lateinit var contexto: Context
    private var idCourse = 0
    private var _binding: FragmentCoursesTemaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var functions: Functions
    private var interstitial:InterstitialAd? = null
    private var count = 0
    private val args: CoursesTemaryFragmentArgs by navArgs()

    private val dashboardViewModel: DashboardViewModel by viewModels()
    @Inject
    lateinit var coursesTemaryAdapter: CoursesTemaryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoursesTemaryBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        functions = Functions(contexto)
        idCourse = args.idCourse
        Glide.with(contexto).load(args.imageCourse).into(binding.iconoCurso)
        binding.titleCourses.text = args.nameCourse
        functions.showHideProgressBar(true, binding.progress)
        setupUI()
        setupShowData()
        initAds()
        initListeners()
        count += 1
        checkCounter()
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
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.getCoursesTemary(idCourse, idUser!!).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.recycler.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        binding.recycler.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recycler.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
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
                binding.textoProgreso.text = resources.getString(R.string.text_progress_course_completed)
            }
            progreso.toInt()==0 -> {
                binding.textoProgreso.text = resources.getString(R.string.text_uninitiated_course)
            }
            else -> {
                binding.textoProgreso.text = resources.getString(R.string.text_progress_course)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }
}