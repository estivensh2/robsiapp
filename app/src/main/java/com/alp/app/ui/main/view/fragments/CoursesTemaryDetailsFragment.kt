package com.alp.app.ui.main.view.fragments

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.alp.app.R
import com.alp.app.data.model.CoursesTemaryDetailsModel
import com.alp.app.data.model.InsertProgressModel
import com.alp.app.databinding.FragmentCoursesTemaryDetailsBinding
import com.alp.app.databinding.TemplateOpenCourseImageBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.CoursesTemaryAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class CoursesTemaryDetailsFragment : Fragment() {

    private var _binding: FragmentCoursesTemaryDetailsBinding? = null
    private val binding get() = _binding!!
    private var idCourseDetails : Int = 0
    private var idCourse : Int = 0
    private lateinit var total : String
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var functions: Functions
    @Inject
    lateinit var coursesTemaryAdapter: CoursesTemaryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoursesTemaryDetailsBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        PreferencesSingleton.init(requireContext(), "preferenciasDeUsuario")
        val args: CoursesTemaryDetailsFragmentArgs by navArgs()
        val bundle = this.arguments
        if (bundle != null) {
            idCourseDetails = bundle.getInt("id", 0)
            idCourse = bundle.getInt("idcurso", 0)
            total = bundle.getString("total", "no")
            if (args.idCourseDetails!=0){
                idCourseDetails = args.idCourseDetails
            }
            if(args.idCourse!=0){
                idCourse = args.idCourse
            }
        }
        binding.imgresultado.setOnClickListener { mostrarImagen() }
        getDetailsTemary()
        return binding.root
    }

    private fun getDetailsTemary() {
        dashboardViewModel.getDetailsTemary(idCourseDetails, idCourse, PreferencesSingleton.leer("id","0").toString()).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderTemary(data) }
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

    private fun insertProgress() {
        dashboardViewModel.setProgress(1, idCourseDetails, idCourse, PreferencesSingleton.leer("id","0").toString()).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderProgress(data) }
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


    private fun renderTemary(data: Response<CoursesTemaryDetailsModel>) {
        val response = data.body()!!
        if (response.data == "1") {
            with(binding){
                tituloTemario.text = response.name
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    descripcionTemario.text = Html.fromHtml(response.description, Html.FROM_HTML_MODE_COMPACT)
                }
                if (response.description.isEmpty()){
                    fondoDescripcionTemario.visibility = View.GONE
                } else {
                    fondoDescripcionTemario.visibility = View.VISIBLE
                }


                if (response.code.isEmpty()){
                    binding.linearLayout2.visibility = View.GONE
                    binding.codeView.visibility = View.GONE
                } else {
                    binding.linearLayout2.visibility = View.VISIBLE
                    binding.codeView.visibility = View.VISIBLE
                    binding.codeView.setOptions(
                        Options.Default.get(requireContext())
                            .withLanguage(response.type_language)
                            .withCode(response.code)
                            .withTheme(ColorTheme.MONOKAI)
                    )
                }
                if (!response.image.contains(".PNG")){
                    binding.textoResultado.visibility = View.GONE
                    binding.contenedorimagen.visibility = View.GONE
                } else {
                    binding.textoResultado.visibility = View.VISIBLE
                    binding.contenedorimagen.visibility = View.VISIBLE
                    Glide.with(contexto).load(response.image).signature(ObjectKey(System.currentTimeMillis())).into(binding.imgresultado)
                }
                btnNext.setOnClickListener {
                    if (PreferencesSingleton.leer("idsonidos", false)==true){
                        val mediaPlayer = MediaPlayer.create(context, R.raw.completado)
                        mediaPlayer.start()
                    }
                    if (idCourseDetails == response.total){
                        val action = CoursesTemaryDetailsFragmentDirections.actionInicioCursosDetalleTemarioFragmentToCoursesReviewFragment(idCourse)
                        it.findNavController().navigate(action)
                    } else {
                        insertProgress()
                        val action = CoursesTemaryDetailsFragmentDirections.actionInicioCursosDetalleTemarioFragmentSelf(idCourseDetails+1, idCourse)
                        it.findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun renderProgress(data: Response<InsertProgressModel>) {
        val response = data.body()!!
        if (response.data == "1") {
            DynamicToast.makeError(contexto, "ya", Toast.LENGTH_LONG).show()
        } else {
            DynamicToast.makeError(contexto, getString(R.string.text_completed_course), Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarImagen() {
        val dialog = Dialog(contexto)
        val bindingBottomSheet = TemplateOpenCourseImageBinding.inflate(layoutInflater, null, false)
        val converterImage = binding.imgresultado.drawable
        bindingBottomSheet.imagenCompleta.setImageDrawable(converterImage)
        bindingBottomSheet.botonCerrar.setOnClickListener { dialog.dismiss() }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(bindingBottomSheet.root)
        dialog.setCancelable(false)
        dialog.show()
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
