package com.alp.app.ui.main.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
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
    private val dashboardViewModel: DashboardViewModel by viewModels()

    private lateinit var item: MenuItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), "preferenciasDeUsuario")
        with(binding){
            // colocamos los argumentos que nos lleguen en sus edittext
            if(args.imagen.isEmpty()){
                Glide.with(contexto).load(R.drawable.usuario).signature(ObjectKey(System.currentTimeMillis())).into(imagen)
            } else {
                Glide.with(contexto).load(args.imagen).signature(ObjectKey(System.currentTimeMillis())).into(imagen)
            }
            nombres.setText(args.nombres)
            claveAcceso.isEnabled = false
            apellidos.setText(args.apellidos)
            claveAcceso.setText(args.claveAcceso)
            correoElectronico.setText(args.correoElectronico)
            switchSonidos.isChecked = PreferencesSingleton.leer("idsonidos" , true) as Boolean
            switchModoOscuro.isChecked = PreferencesSingleton.leer("idoscuro" , true) as Boolean
            //
            botonSubirImagen.setOnClickListener  { alertaSubirImagen() }
            botonCerrarSesion.setOnClickListener { ventanaCerrarSesion() }
           botonCambiarClave.setOnClickListener { v -> Navigation.findNavController(v).navigate(R.id.accion_configuracion_a_cambiar_clave) }
            switchSonidos.setOnCheckedChangeListener { _, isChecked ->
                when(isChecked){
                    true ->  PreferencesSingleton.escribir("idsonidos", true)
                    false -> PreferencesSingleton.escribir("idsonidos", false)
                }
            }
            imagen.setOnClickListener { mostrarImagen() }
            switchModoOscuro.setOnCheckedChangeListener { v, isChecked ->
                when(isChecked){
                    true ->  {
                        PreferencesSingleton.escribir("idoscuro", true)
                        Navigation.findNavController(v).navigate(R.id.action_perfilDetalleFragment_self)
                    }
                    false -> {
                        PreferencesSingleton.escribir("idoscuro", false)
                        Navigation.findNavController(v).navigate(R.id.action_perfilDetalleFragment_self)
                    }
                }
            }
            when (args.idnotificaciones) {
                "1" -> switchNotificaciones.isChecked = true
                "2" -> switchNotificaciones.isChecked = false
            }
            botonacercade.setOnClickListener { mostrarBottomSheet() }
        }
        setHasOptionsMenu(true)
        return binding.root
    }


    private fun setupShowData() {
        val nombres = binding.nombres.text.toString()
        if (bitmap!=null) { imagen = convertirImagenABase64(bitmap!!) }
        val apellidos = binding.apellidos.text.toString()
        val correo = binding.correoElectronico.text.toString()
        binding.progress.visibility = View.VISIBLE
        dashboardViewModel.setInfoProfile(PreferencesSingleton.leer("id","0").toString(),nombres, imagen, apellidos, correo).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: Response<UpdateInfoModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            DynamicToast.makeSuccess(contexto, getString(R.string.texto_datos_actualizados), Toast.LENGTH_LONG).show()
        } else {
            DynamicToast.makeError(contexto, getString(R.string.texto_error_datos), Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarImagen() {
        val dialogo = Dialog(contexto)
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogo.setContentView(R.layout.ventana_abrir_imagen)
        dialogo.setCancelable(false)
        dialogo.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        dialogo.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val image = dialogo.findViewById<ImageView>(R.id.imagenCompleta)
        val close = dialogo.findViewById<Button>(R.id.botonCerrar)
        val converter = binding.imagen.drawable
        image.setImageDrawable(converter)
        close.setOnClickListener { dialogo.dismiss() }
        dialogo.show()
    }

    @SuppressLint("InflateParams")
    private fun mostrarBottomSheet() {
        val view = layoutInflater.inflate(R.layout.ventana_acerde_de, null)
        val dialog = BottomSheetDialog(contexto)
        val button = view.findViewById<Button>(R.id.boton_donar)
        button.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/donate?hosted_button_id=3WMF7PP7FMGVQ"))) }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun ventanaCerrarSesion() {
        val vistaDialogo = layoutInflater.inflate(R.layout.dialogo_cerrar_sesion, null)
        val botonNo = vistaDialogo.findViewById<Button>(R.id.botonNo)
        val botonSi = vistaDialogo.findViewById<Button>(R.id.botonSi)
        val dialogo = AlertDialog.Builder(context).setCancelable(false).create()
        botonNo.setOnClickListener { dialogo.dismiss() }
        botonSi.setOnClickListener {
            PreferencesSingleton.eliminar("sesionActiva")
            startActivity(Intent(contexto, HomeActivity::class.java))
            activity?.finish()
        }
        dialogo.setView(vistaDialogo)
        dialogo.show()
    }

    private fun alertaSubirImagen() {
        val opciones = arrayOf("Tomar Foto", "Elegir Foto", "Cancelar")
        val alertaDialogo = AlertDialog.Builder(context)
        alertaDialogo.setTitle("Elige una opción")
        alertaDialogo.setCancelable(false)
        alertaDialogo.setItems(opciones) { dialogo, posicion ->
            when (opciones[posicion]) {
                "Tomar Foto"  -> abrirCamara()
                "Elegir Foto" -> abrirGaleria()
                "Cancelar"    -> dialogo.dismiss()
            }
        }
        alertaDialogo.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        item = menu.findItem(R.id.guardar)
        item.isVisible = false
        with(binding){
            nombres.onChange           { habilitarBoton(nombres, args.nombres, item) }
            apellidos.onChange         { habilitarBoton(apellidos, args.apellidos, item) }
            correoElectronico.onChange { habilitarBoton(correoElectronico, args.correoElectronico, item) }
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun habilitarBoton(parametro: EditText, valor:String, item: MenuItem) {
        item.isVisible = parametro.text.toString()!=valor
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun convertirImagenABase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imgBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgBytes, Base64.DEFAULT)
    }

        private fun abrirGaleria() {
        if (ContextCompat.checkSelfPermission(contexto.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermisosGaleria()
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, codeSelectPhoto)
        }
    }

    private fun abrirCamara() {
        if (ContextCompat.checkSelfPermission(contexto.applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermisosCamara()
        } else {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), capturePhoto)
        }
    }

    private fun solicitarPermisosGaleria() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DynamicToast.makeError(contexto, getString(R.string.texto_activar_permisos), Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), selectPhotoPermission)
        }
    }

    private fun solicitarPermisosCamara() {
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