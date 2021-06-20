package com.alp.app.ui.main.view.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.alp.app.ui.main.view.activities.HomeActivity
import com.alp.app.R
import com.alp.app.data.model.UpdateInfoModel
import com.alp.app.databinding.FragmentProfileDetailsBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment() {

    private val binding get() = _binding!!
    private val capturePhoto = 102
    private var imagen:String = ""
    private var bitmap: Bitmap? = null
    private val codeCapturePhoto = 2
    private val codeSelectPhoto = 103
    private val selectPhotoPermission = 3
    private val args: ProfileDetailsFragmentArgs by navArgs()
    private var _binding: FragmentProfileDetailsBinding? = null
    private lateinit var contexto: Context
    private lateinit var functions: Functions
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var item: MenuItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), "preferenciasDeUsuario")
        functions = Functions(contexto)
        with(binding){
            if(args.imagen.isEmpty()){
                Glide.with(contexto).load(R.drawable.user_test).signature(ObjectKey(System.currentTimeMillis())).into(imagen)
            } else {
                Glide.with(contexto).load(args.imagen).signature(ObjectKey(System.currentTimeMillis())).into(imagen)
            }
            iENames.setText(args.nombres)
            iEPassword.isEnabled = false
            iELastNames.setText(args.apellidos)
            iEPassword.setText(args.claveAcceso)
            iEEmail.setText(args.correoElectronico)
            sounds.isChecked = PreferencesSingleton.leer("idsonidos" , true) as Boolean
            botonSubirImagen.setOnClickListener  { alertaSubirImagen() }
            botonCerrarSesion.setOnClickListener {
                PreferencesSingleton.eliminar("sesionActiva")
                startActivity(Intent(contexto, HomeActivity::class.java))
                activity?.finish()
            }
            iLPassword.setOnClickListener { v -> Navigation.findNavController(v).navigate(R.id.accion_configuracion_a_cambiar_clave) }
            sounds.setOnCheckedChangeListener { _, isChecked ->
                when(isChecked){
                    true ->  PreferencesSingleton.escribir("idsonidos", true)
                    false -> PreferencesSingleton.escribir("idsonidos", false)
                }
            }
            when (args.idnotificaciones) {
                "1" -> notifications.isChecked = true
                "2" -> notifications.isChecked = false
            }
            changeTheme.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    PreferencesSingleton.escribir("mode", true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    PreferencesSingleton.escribir("mode", false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupShowData() {
        val nombres = binding.iENames.text.toString()
        if (bitmap!=null) { imagen = ConvertImageToBase64(bitmap!!) }
        val apellidos = binding.iELastNames.text.toString()
        val correo = binding.iEPassword.text.toString()
        binding.progress.visibility = View.VISIBLE
        dashboardViewModel.setInfoProfile(PreferencesSingleton.leer("id","0").toString(),nombres, imagen, apellidos, correo).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderList(data: Response<UpdateInfoModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            DynamicToast.makeSuccess(contexto, getString(R.string.texto_datos_actualizados), Toast.LENGTH_LONG).show()
        } else {
            DynamicToast.makeError(contexto, getString(R.string.texto_error_datos), Toast.LENGTH_LONG).show()
        }
    }

    private fun alertaSubirImagen() {
        val options = arrayOf(resources.getString(R.string.text_capture_image), resources.getString(R.string.text_choose_image), resources.getString(R.string.text_cancel))
        MaterialAlertDialogBuilder(contexto)
                .setTitle(resources.getString(R.string.text_option_choose))
                .setCancelable(false)
                .setItems(options) { dialog, position ->
                    when(options[position]){
                        resources.getString(R.string.text_capture_image) -> openCamera()
                        resources.getString(R.string.text_choose_image)  -> loadGallery()
                        resources.getString(R.string.text_cancel)        -> dialog.dismiss()
                    }
                }
                .show()
    }

    private fun ConvertImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imgBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgBytes, Base64.DEFAULT)
    }

    private fun loadGallery() {
        if (ContextCompat.checkSelfPermission(contexto.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsGallery()
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, codeSelectPhoto)
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(contexto.applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsCamera()
        } else {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), capturePhoto)
        }
    }

    private fun requestPermissionsGallery() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DynamicToast.makeError(contexto, getString(R.string.texto_activar_permisos), Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), selectPhotoPermission)
        }
    }

    private fun requestPermissionsCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
            DynamicToast.makeSuccess(contexto, getString(R.string.texto_activar_permisos), Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), codeCapturePhoto)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == selectPhotoPermission) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "si", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "no", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null){
            val imagenSeleccionada = data.data
            try {
                when(requestCode){
                    codeSelectPhoto -> {
                        bitmap = MediaStore.Images.Media.getBitmap(contexto.contentResolver, imagenSeleccionada)
                        binding.imagen.setImageBitmap(bitmap)
                        item.isVisible = true
                    }
                    capturePhoto -> {
                        bitmap = data.extras?.get("data") as Bitmap
                        binding.imagen.setImageBitmap(bitmap)
                        item.isVisible = true
                    }
                }
            } catch (e: IOException) {
                if (bitmap!=null) binding.imagen.setImageBitmap(bitmap)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_configuracion, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.guardar -> setupShowData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

}