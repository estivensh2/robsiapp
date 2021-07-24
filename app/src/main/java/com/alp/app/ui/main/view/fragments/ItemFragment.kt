/*
 * *
 *  * Created by estiv on 7/07/21 04:54 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 7/07/21 04:54 PM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.alp.app.R
import com.alp.app.data.model.*
import com.alp.app.databinding.FragmentItemBinding
import com.alp.app.databinding.TemplateReportBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM5 = "param5"
private const val ARG_PARAM6 = "param6"
private const val ARG_PARAM7 = "param7"
private const val ARG_PARAM8 = "param8"

@AndroidEntryPoint
class ItemFragment : Fragment(){

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!
    private var param1: Int? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: Int? = null
    private var param5: Int? = null
    private var param6: Int? = null
    private var param7: Int? = null
    private var param8: Int? = null
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var contexto: Context
    private lateinit var functions: Functions
    private var onButtonClickListener: OnButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
            param4 = it.getInt(ARG_PARAM4)
            param5 = it.getInt(ARG_PARAM5)
            param6 = it.getInt(ARG_PARAM6)
            param7 = it.getInt(ARG_PARAM7)
            param8 = it.getInt(ARG_PARAM8)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        functions = Functions(requireContext())
        binding.comments.setOnClickListener {
            val idUser = PreferencesSingleton.read("id_user", 0)
            val action = DetailTopicFragmentDirections.actionDetailTopicFragmentToCommentsCourseFragment(idUser!!,param1!!)
            it.findNavController().navigate(action)
        }
        binding.btnNext.setOnClickListener {
            onButtonClickListener?.onButtonClicked(param6, param1)
        }
        binding.bookMark.isChecked = param8 == 1
        binding.bookMark.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                changeFavorite(1)
                showSnackBar(getString(R.string.text_add_favorite))
            } else {
                changeFavorite(0)
                showSnackBar(getString(R.string.text_delete_favorite))
            }
        }
        binding.report.setOnClickListener {
            showDialog()
        }
        val head = "<html><head>"
        val style = "<style type='text/css'>" +
                "@import url('https://fonts.googleapis.com/css2?family=Changa:wght@200;300;400;500;600;700;800&display=swap');" +
                "body { font-family: 'Changa', sans-serif; }" +
                "</style>" +
                "<link href=\"http://192.168.0.18/backendalp/public/vendor/ckeditor/plugins/codesnippet/lib/highlight/styles/monokai_sublime.css\" rel=\"stylesheet\">"
        val endHead = "</head>"
        val script = "" +
                "<script src=\"http://192.168.0.18/backendalp/public/vendor/ckeditor/plugins/codesnippet/lib/highlight/highlight.pack.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script>"
        val body = "<body>$param2\n\n$script</body></html>"
        val myHtmlString = head + style + endHead + body
        with(binding){
            webView.loadDataWithBaseURL(null, myHtmlString, "text/html", "UTF-8", null)
            webView.settings.javaScriptEnabled = true
            level.text = param3
            visits.text = resources.getString(R.string.text_visits, param4)
            numberComments.text = resources.getString(R.string.text_comments, param5)
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
        binding.share.setOnClickListener {
            share(screenShot(requireActivity().window.decorView.rootView))
        }
        setupShowData()
        return binding.root
    }

    private fun screenShot(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun share(bitmap: Bitmap) {
        val text = resources.getString(R.string.app_name)
        val calendar = Calendar.getInstance().time
        val pathOfBmp = MediaStore.Images.Media.insertImage(contexto.contentResolver, bitmap, "$text - Capture $calendar", null)
        val uri: Uri = Uri.parse(pathOfBmp)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "$text - Capture $calendar")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "$text - Capture $calendar")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        contexto.startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.text_share)))
    }

    private fun changeFavorite(active: Int) {
        val idUser = PreferencesSingleton.read("id_user", 0)!!
        dashboardViewModel.changeFavorite(active, param1!!, idUser, param7!!).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { data -> successFavorite(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        //
                    }
                }
            }
        }
    }

    private fun successFavorite(data: Response<ChangeFavoriteModel>) {
        if (data.body()!!.response == 1){
            Log.i("response", getString(R.string.text_inserted))
        } else {
            Log.i("response", getString(R.string.text_updated))
        }
    }

    private fun setupShowData() {
        dashboardViewModel.insertVisit(param1!!).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { data -> insertVisit(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        //functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun sendReport(report: String, comment: String, alertDialog: AlertDialog, binding: TemplateReportBinding) {
        val idUser = PreferencesSingleton.read("id_user", 0)!!
        dashboardViewModel.sendReport(report, comment, param1!!, idUser).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> insertReport(data,alertDialog) }
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

    private fun insertReport(data: Response<ReportDetailTopicModel>, alertDialog: AlertDialog) {
        if (data.body()!!.response == 1){
            alertDialog.dismiss()
            DynamicToast.makeSuccess(contexto, getString(R.string.text_send_report), Toast.LENGTH_LONG).show()
        } else {
            DynamicToast.makeError(contexto, getString(R.string.text_error_send), Toast.LENGTH_LONG).show()
        }
    }

    private fun insertVisit(data: Response<InsertVisitModel>) {
        if (data.body()!!.response == 1){
            Log.i("responseVisit", "inserted")
        } else {
            Log.i("responseVisit", "notInserted")
        }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(contexto)
        val binding = TemplateReportBinding.inflate(layoutInflater, null, false)
        dialog.setView(binding.root)
        val alertDialog = dialog.create()
        binding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.btnReport.isEnabled = false
        var radioButton: RadioButton? = null
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.btnReport.isEnabled = group.checkedRadioButtonId != -1
            radioButton = group.findViewById(checkedId)
        }
        binding.btnReport.setOnClickListener {
            sendReport(radioButton?.text.toString(), binding.iEMessageReport.text.toString(), alertDialog, binding)
        }
        alertDialog.show()
    }

    private fun showSnackBar(text: String){
        val snackBar = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
        val layoutParams = LinearLayout.LayoutParams(snackBar.view.layoutParams)
        layoutParams.gravity = Gravity.TOP
        snackBar.view.setPadding(0, 10, 0, 0)
        snackBar.view.layoutParams = layoutParams
        snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBar.show()
    }

    interface OnButtonClickListener {
        fun onButtonClicked(index: Int?, id_detail_topic: Int?)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onButtonClickListener = parentFragment as OnButtonClickListener?
        this.contexto = context
    }

    companion object {
        @JvmStatic fun newInstance(data: DetailTopicModel, position: Int, idCourse: Int) = ItemFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1,    data.id_detail_topic)
                putString(ARG_PARAM2, data.description)
                putString(ARG_PARAM3, data.level)
                putInt(ARG_PARAM4,    data.visits)
                putInt(ARG_PARAM5,    data.comments)
                putInt(ARG_PARAM6,    position)
                putInt(ARG_PARAM7,    idCourse)
                putInt(ARG_PARAM8,    data.favorite)
            }
        }
    }
}



