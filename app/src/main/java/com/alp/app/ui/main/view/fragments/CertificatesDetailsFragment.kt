/*
 * *
 *  * Created by estiv on 30/07/21, 7:37 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 30/07/21, 7:37 p. m.
 *
 */

package com.alp.app.ui.main.view.fragments

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alp.app.R
import com.alp.app.data.model.CertificateDetailModel
import com.alp.app.databinding.FragmentCertificatesDetailsBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response


@AndroidEntryPoint
class CertificatesDetailsFragment : Fragment() {

    private var _binding: FragmentCertificatesDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private lateinit var functions: Functions
    private val args: CertificatesDetailsFragmentArgs by navArgs()
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var url = ""
    private var myDownload: Long = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificatesDetailsBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        setupShowData()
        with(binding) {
            btnCopy.setOnClickListener {
                functions.copyToClipboard(iEUrl.text.toString())
                btnCopy.text = getString(R.string.text_copied)
            }
            webView.apply {
                settings.javaScriptEnabled = true
                settings.setSupportZoom(false)
                webChromeClient = WebChromeClient()
                loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$url")
            }
            btnShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "application/pdf"
                intent.putExtra(Intent.EXTRA_TEXT, url)
                contexto.startActivity(Intent.createChooser(intent, resources.getString(R.string.text_share)))
            }
            btnSave.setOnClickListener {
                val request = DownloadManager.Request(Uri.parse(url))
                val title = URLUtil.guessFileName(url, null, null)
                val cookie = CookieManager.getInstance().getCookie(url)
                request.setTitle(title)
                request.setDescription(getString(R.string.text_wait_download))
                request.addRequestHeader("cookie", cookie)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
                val downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                myDownload = downloadManager.enqueue(request)
            }
            val broadCastReceiver = object : BroadcastReceiver(){
                override fun onReceive(context: Context?, intent: Intent?) {
                    val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == myDownload){
                        Toast.makeText(contexto, getString(R.string.text_completed_download), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            requireActivity().registerReceiver(broadCastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }
        return binding.root
    }

    private fun setupShowData() {
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.certificateDetail(idUser!!, args.idCourse).observe(requireActivity()) { response ->
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

    private fun renderList(data: Response<CertificateDetailModel>) {
        val body = data.body()!!
        if (data.isSuccessful){
            url = body.url
            binding.iEUrl.setText(url)
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