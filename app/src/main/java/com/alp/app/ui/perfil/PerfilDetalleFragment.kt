package com.alp.app.ui.perfil

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alp.app.R
import com.alp.app.data.RespuestaActualizarDatos
import com.alp.app.databinding.FragmentPerfilDetalleBinding
import com.alp.app.servicios.*
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException

class PerfilDetalleFragment : Fragment() {

    private val binding get() = _binding!!
    private val TOMAR_FOTO = 102
    private var imagen:String = ""
    private var bitmap: Bitmap? = null
    private val TOMAR_FOTO_PERMISO = 2
    private val SELECCIONAR_FOTO = 103
    private val SELECCIONAR_FOTO_PERMISO = 3
    private val args: PerfilDetalleFragmentArgs by navArgs()
    private var _binding: FragmentPerfilDetalleBinding? = null
    private lateinit var contexto: Context
    private lateinit var viewModel: PerfilDetalleViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilDetalleBinding.inflate(inflater, container, false)
        Preferencias.init(requireContext(), "preferenciasDeUsuario")
        with(binding){
            // colocamos los argumentos que nos lleguen en sus edittext
            if(args.imagen.isEmpty()){
                Glide.with(contexto).load(R.drawable.boton_facebook_claro).signature(ObjectKey(System.currentTimeMillis())).into(imagen)
            } else {
                Glide.with(contexto).load(args.imagen).signature(ObjectKey(System.currentTimeMillis())).into(imagen)
            }
            nombres.setText(args.nombres)
            claveAcceso.isEnabled = false
            apellidos.setText(args.apellidos)
            claveAcceso.setText(args.claveAcceso)
            correoElectronico.setText(args.correoElectronico)
            switchSonidos.isChecked = Preferencias.leer("idsonidos" , true) as Boolean
            switchModoOscuro.isChecked = Preferencias.leer("idoscuro" , true) as Boolean
            //
            botonSubirImagen.setOnClickListener  { alertaSubirImagen() }
            botonCerrarSesion.setOnClickListener { ventanaCerrarSesion() }
            botonCambiarClave.setOnClickListener { v -> Navigation.findNavController(v).navigate(R.id.accion_configuracion_a_cambiar_clave) }
            switchSonidos.setOnCheckedChangeListener { _, isChecked ->
                when(isChecked){
                    true ->  Preferencias.escribir("idsonidos", true)
                    false -> Preferencias.escribir("idsonidos", false)
                }
            }
            switchModoOscuro.setOnCheckedChangeListener { v, isChecked ->
                when(isChecked){
                    true ->  {
                        Preferencias.escribir("idoscuro", true)
                        Navigation.findNavController(v).navigate(R.id.action_perfilDetalleFragment_self)
                    }
                    false -> {
                        Preferencias.escribir("idoscuro", false)
                        Navigation.findNavController(v).navigate(R.id.action_perfilDetalleFragment_self)
                    }
                }
            }
            when (args.idnotificaciones) {
                "1" -> switchNotificaciones.isChecked = true
                "2" -> switchNotificaciones.isChecked = false
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun ventanaCerrarSesion() {
        val vistaDialogo = layoutInflater.inflate(R.layout.dialogo_cerrar_sesion, null)
        val botonNo = vistaDialogo.findViewById<Button>(R.id.botonNo)
        val botonSi = vistaDialogo.findViewById<Button>(R.id.botonSi)
        val dialogo = AlertDialog.Builder(context).setCancelable(false).create()
        botonNo.setOnClickListener { dialogo.dismiss() }
        botonSi.setOnClickListener { v ->
            Preferencias.eliminar("sesionActiva")
            Navigation.findNavController(v).navigate(R.id.accion_configuracion_a_iniciar_o_crear)
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
        val item = menu.findItem(R.id.guardar)
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

    private fun actualizarDatos() {
        val nombres = binding.nombres.text.toString()
        if (bitmap!=null) { imagen = convertirImagenABase64(bitmap!!) }
        val apellidos = binding.apellidos.text.toString()
        val correo = binding.correoElectronico.text.toString()
        binding.cargaContenido.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val call = ServicioBuilder.buildServicio(APIServicio::class.java)
            try {
                call.actualizarDatos(Preferencias.leer("id","0").toString(),nombres, imagen, apellidos, correo).enqueue(object : Callback<RespuestaActualizarDatos> {
                    override fun onResponse(call: Call<RespuestaActualizarDatos>, response: Response<RespuestaActualizarDatos>) {
                        val responsex = response.body()!!
                        activity?.runOnUiThread {
                            if (responsex.respuesta == "1") {
                                binding.cargaContenido.visibility = View.GONE
                                ClaseToast.mostrarx(contexto, getString(R.string.texto_datos_actualizados), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            } else {
                                binding.cargaContenido.visibility = View.GONE
                                ClaseToast.mostrarx(contexto, getString(R.string.texto_error_datos), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            }
                        }
                    }
                    override fun onFailure(call: Call<RespuestaActualizarDatos>, t: Throwable) {
                        activity!!.runOnUiThread {
                            ClaseToast.mostrarx(contexto, getString(R.string.texto_error_conexion), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                            binding.cargaContenido.visibility = View.VISIBLE
                        }
                    }
                })
            } catch (e: Throwable) {
                requireActivity().runOnUiThread {
                    ClaseToast.mostrarx(contexto, getString(R.string.texto_error_grave), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
                    binding.cargaContenido.visibility = View.VISIBLE
                }
            }
        }
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
            startActivityForResult(intent, SELECCIONAR_FOTO)
        }
    }

    private fun abrirCamara() {
        if (ContextCompat.checkSelfPermission(contexto.applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermisosCamara()
        } else {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), TOMAR_FOTO)
        }
    }

    private fun solicitarPermisosGaleria() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ClaseToast.mostrarx(contexto, getString(R.string.texto_activar_permisos), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), SELECCIONAR_FOTO_PERMISO)
        }
    }

    private fun solicitarPermisosCamara() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
            ClaseToast.mostrarx(contexto, getString(R.string.texto_activar_permisos), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), TOMAR_FOTO_PERMISO)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == SELECCIONAR_FOTO_PERMISO) {
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
                    SELECCIONAR_FOTO -> {
                        bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imagenSeleccionada)
                        binding.imagen.setImageBitmap(bitmap)
                    }
                    TOMAR_FOTO -> {
                        bitmap = data.extras?.get("data") as Bitmap
                        binding.imagen.setImageBitmap(bitmap)
                    }
                }
            } catch (e: IOException) {
                if (bitmap!=null) binding.imagen.setImageBitmap(bitmap)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PerfilDetalleViewModel::class.java)
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
            R.id.guardar -> actualizarDatos()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

}