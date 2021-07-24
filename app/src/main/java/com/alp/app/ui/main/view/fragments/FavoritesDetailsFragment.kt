/*
 * *
 *  * Created by estiv on 21/07/21, 5:49 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 21/07/21, 5:49 p. m.
 *
 */

package com.alp.app.ui.main.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.alp.app.R
import com.alp.app.data.model.DetailTopicFavoriteModel
import com.alp.app.databinding.FragmentFavoritesDetailsBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class FavoritesDetailsFragment : Fragment() {

    private var _binding: FragmentFavoritesDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val args : FavoritesDetailsFragmentArgs by navArgs()
    private lateinit var functions: Functions
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesDetailsBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        setupShowData()
        return binding.root
    }

    private fun setupShowData() {
        dashboardViewModel.getDetailTopicFavorite(args.idDetailTopic).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun renderList(response: Response<DetailTopicFavoriteModel>) {
        val data = response.body()!!
        val head = "<html><head>"
        val style = "<style type='text/css'>" +
                "@import url('https://fonts.googleapis.com/css2?family=Changa:wght@200;300;400;500;600;700;800&display=swap');" +
                "body { font-family: 'Changa', sans-serif; }" +
                "</style>"
        val endHead = "</head>"
        val body = "<body>${data.description}</body></html>"
        val myHtmlString = head + style + endHead + body
        if (response.isSuccessful){
            with(binding){
                visits.text = resources.getString(R.string.text_visits, data.visits)
                level.text = data.level
                webView.loadDataWithBaseURL(null, myHtmlString, "text/html", "UTF-8", null)
                webView.settings.javaScriptEnabled = true
                webView.setBackgroundColor(Color.TRANSPARENT)
            }
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    WebSettingsCompat.setForceDark(binding.webView.settings, WebSettingsCompat.FORCE_DARK_ON)
                } else {
                    WebSettingsCompat.setForceDark(binding.webView.settings, WebSettingsCompat.FORCE_DARK_OFF)
                }
            }
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