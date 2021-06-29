package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alp.app.R
import com.alp.app.data.model.InsertCertificateModel
import com.alp.app.data.model.ReviewModel
import com.alp.app.databinding.BottomSheetQuestionBinding
import com.alp.app.databinding.FragmentCoursesReviewBinding
import com.alp.app.databinding.VentanaBottomSheetBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.ReviewAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class CoursesReviewFragment : Fragment() {

    private var _binding: FragmentCoursesReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var functions : Functions
    private var idcourse: Int = 0
    private var interstitial:InterstitialAd? = null
    private var count = 0
    var lastClickTime: Long = 0
    @Inject
    lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoursesReviewBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        val args: CoursesReviewFragmentArgs by navArgs()
        if (args.idCourse!=0){
            idcourse = args.idCourse
        }
        binding.title.text = ""
        setupUI()
        getReview()
        initAds()
        initListeners()
        checkCounter()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (lastClickTime.plus(2000L) > System.currentTimeMillis()) {
                    alertDialog()
                } else {
                    DynamicToast.makeWarning(contexto, resources.getString(R.string.text_click_again)).show()
                    lastClickTime = System.currentTimeMillis()
                }
            }
        })
        return binding.root
    }

    private fun alertDialog(){
        MaterialAlertDialogBuilder(contexto)
                .setMessage(resources.getString(R.string.text_cancel_review))
                .setNegativeButton(resources.getString(R.string.text_no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.text_yes)){ _, _ ->
                    findNavController().navigate(R.id.action_coursesReviewFragment_to_homeFragment)
                }
                .setCancelable(false)
                .show()
    }

    private fun setupUI() {
        with(binding){
            reviewAdapter  = ReviewAdapter(contexto)
            reviewAdapter.onItemClick = {
                if(reviewAdapter.optionSelected == it.response){
                    val list = listOf("¡Eres lo máximo!", "¡Correcto!", "¡Buen trabajo!")
                    val position = (0..2).random()
                    if(binding.viewPager2.currentItem == reviewAdapter.list.size-1){
                        insertCertificate()
                    } else {
                        loadBottomSheet(list[position], R.drawable.ic_baseline_check_circle_24, true)
                    }
                } else {
                    val list = listOf("Ups, inténtalo de nuevo", "La respuesta es incorrecta", "Hmm, piénsalo de nuevo")
                    val position = (0..2).random()
                    loadBottomSheet(list[position], R.drawable.ic_baseline_cancel_24, false)
                }
            }

            viewPager2.apply {
                adapter = reviewAdapter
                isUserInputEnabled = false
            }

            TabLayoutMediator(indicator, viewPager2) { tab, _ ->
                tab.icon = ContextCompat.getDrawable(contexto, R.drawable.ic_baseline_help_24)
                tab.view.isEnabled = false
            }.attach()
        }
    }


    private fun insertCertificate() {
        dashboardViewModel.setCertificate(PreferencesSingleton.leer("id","0").toString(), idcourse,"1").observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderCertificate(data) }
                    }
                    Status.ERROR   -> {
                        functions.showHideProgressBar(false, binding.progress)
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderCertificate(data: Response<InsertCertificateModel>) {
        val response = data.body()!!
        if (response.data == "1") {
            bottomSheetCertificate()
        } else {
            findNavController().navigate(R.id.action_coursesReviewFragment_to_profile)
            DynamicToast.makeSuccess(contexto, getString(R.string.text_active_certificate), Toast.LENGTH_LONG).show()
        }
    }

    private fun bottomSheetCertificate() {
        val dialog = BottomSheetDialog(contexto)
        val bindingBottomSheet = VentanaBottomSheetBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(bindingBottomSheet.root)
        dialog.setCancelable(false)
        dialog.show()
        bindingBottomSheet.seeCertificates.setOnClickListener {
            findNavController().navigate(R.id.action_coursesReviewFragment_to_homeFragment)
            dialog.dismiss()
        }
    }

    private fun loadBottomSheet(message: String, icon: Int, boolean: Boolean){
        val dialog = BottomSheetDialog(contexto)
        val bindingBottomSheet = BottomSheetQuestionBinding.inflate(layoutInflater, null, false)
        bindingBottomSheet.textResult.text = message
        if (boolean){
            count += 1
            val mediaPlayer = MediaPlayer.create(context, R.raw.completado)
            mediaPlayer.start()
            bindingBottomSheet.buttonResult.text = resources.getString(R.string.text_continue)
            bindingBottomSheet.buttonResult.setOnClickListener {
                dialog.dismiss()
                val position = binding.viewPager2.currentItem
                binding.viewPager2.currentItem = position+1
            }
        } else {
            count += 1
            bindingBottomSheet.buttonResult.text = resources.getString(R.string.text_retry)
            bindingBottomSheet.buttonResult.setOnClickListener {
                dialog.dismiss()
            }
        }
        bindingBottomSheet.imgResult.setImageResource(icon)
        dialog.setCancelable(false)
        dialog.setContentView(bindingBottomSheet.root)
        dialog.show()
    }

    private fun getReview() {
        dashboardViewModel.getReview(idcourse).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        functions.showHideProgressBar(false, binding.progress)
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderList(data: List<ReviewModel>) {
        reviewAdapter.apply {
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