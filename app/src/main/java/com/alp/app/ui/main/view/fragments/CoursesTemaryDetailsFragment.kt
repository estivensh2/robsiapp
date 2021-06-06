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
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.InsertCertificateModel
import com.alp.app.data.model.InsertProgressModel
import com.alp.app.databinding.FragmentCoursesTemaryDetailsBinding
import com.alp.app.singleton.ClaseToast
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import retrofit2.Response


@AndroidEntryPoint
class CoursesTemaryDetailsFragment : Fragment() {

    private var _binding: FragmentCoursesTemaryDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var id : String
    private lateinit var total : String
    private lateinit var idcurso : String
    private lateinit var contexto: Context
    private lateinit var ultimoelemento : String
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoursesTemaryDetailsBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), "preferenciasDeUsuario")
        val bundle = this.arguments
        if (bundle != null) {
            id = bundle.getString("id", "no")
            idcurso = bundle.getString("idcurso", "no")
            total = bundle.getString("total", "no")
            ultimoelemento = bundle.getString("ultimoelemento", "no")
            val nombre = bundle.getString("nombre", "no")
            val descripcion = bundle.getString("descripcion", "no")
            val tipolenguaje = bundle.getString("tipolenguaje", "no")
            val codigo = bundle.getString("codigo", "no")
            val imgresultado = bundle.getString("imgresultado", "no")
            binding.tituloTemario.text = nombre
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                binding.descripcionTemario.setText(Html.fromHtml(descripcion, Html.FROM_HTML_MODE_COMPACT))
            }
            if (!imgresultado.contains(".PNG")){
                binding.textoResultado.visibility = View.GONE
                binding.contenedorimagen.visibility = View.GONE
            } else {
                binding.textoResultado.visibility = View.VISIBLE
                binding.contenedorimagen.visibility = View.VISIBLE
                Glide.with(contexto).load(imgresultado).signature(ObjectKey(System.currentTimeMillis())).into(binding.imgresultado)
            }
            if (descripcion.isEmpty()){
                binding.fondoDescripcionTemario.visibility = View.GONE
                binding.fondoDescripcionTemario.visibility = View.GONE
            } else {
                binding.fondoDescripcionTemario.visibility = View.VISIBLE
            }
            if (codigo.isEmpty()){
                binding.linearLayout2.visibility = View.GONE
                binding.codeView.visibility = View.GONE
            } else {
                binding.linearLayout2.visibility = View.VISIBLE
                binding.codeView.visibility = View.VISIBLE
                binding.codeView.setOptions(
                    Options.Default.get(requireContext())
                        .withLanguage(tipolenguaje)
                        .withCode(codigo)
                        .withTheme(ColorTheme.MONOKAI)
                )
            }
        }
        binding.imgresultado.setOnClickListener { mostrarImagen() }
        binding.completar.setOnClickListener {
            if (PreferencesSingleton.leer("idsonidos", false)==true){
                val mediaPlayer = MediaPlayer.create(context, R.raw.completado)
                mediaPlayer.start()
            }
            if (total==ultimoelemento){
                insertCertificate()
            } else {
                insertProgress()
                //realizarExamen()
            }
        }
        return binding.root
    }


    private fun insertProgress() {
        dashboardViewModel.setProgress("1", id, idcurso, PreferencesSingleton.leer("id","0").toString()).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        showHideProgressBar(false)
                        resource.data?.let { data -> renderProgress(data) }
                    }
                    Status.ERROR   -> {
                        showMessage(response.message!!)
                        showHideProgressBar(false)
                    }
                    Status.LOADING -> {
                        showHideProgressBar(true)
                    }
                }
            }
        })
    }

    private fun insertCertificate() {
        dashboardViewModel.setCertificate(PreferencesSingleton.leer("id","0").toString(), idcurso,"1").observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        showHideProgressBar(false)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        showMessage(response.message!!)
                        showHideProgressBar(false)
                    }
                    Status.LOADING -> {
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

    private fun renderList(data: Response<InsertCertificateModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            mostrarBottomSheet()
        } else {
            ClaseToast.mostrarx(contexto, getString(R.string.texto_diploma_activo), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
        }
    }

    private fun renderProgress(data: Response<InsertProgressModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            mostrarBottomSheetx()
        } else {
            ClaseToast.mostrarx(contexto, getString(R.string.texto_error_completado_anteriormente), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
        }
    }

    private fun mostrarImagen() {
        val dialogo = Dialog(contexto)
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogo.setContentView(R.layout.ventana_abrir_imagen)
        dialogo.setCancelable(false)
        dialogo.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        dialogo.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val imagen = dialogo.findViewById<ImageView>(R.id.imagenCompleta)
        val cerrar = dialogo.findViewById<Button>(R.id.botonCerrar)
        val convertir = binding.imgresultado.drawable
        imagen.setImageDrawable(convertir)
        cerrar.setOnClickListener { dialogo.dismiss() }
        dialogo.show()
    }

    private fun mostrarBottomSheet() {
        val view = layoutInflater.inflate(R.layout.ventana_bottom_sheet, null)
        val dialogo = BottomSheetDialog(contexto)
        val boton = view.findViewById<Button>(R.id.ver_diplomas)
        dialogo.setContentView(view)
        dialogo.show()
        boton.setOnClickListener {
            findNavController().navigate(R.id.accion_inicio_cursos_detalle_a_diplomados)
            dialogo.dismiss()
        }
    }

    private fun mostrarBottomSheetx() {
        val view = layoutInflater.inflate(R.layout.ventana_temario_completado, null)
        val dialogo = BottomSheetDialog(contexto)
        dialogo.setContentView(view)
        dialogo.show()
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
