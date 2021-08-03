/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 12:21 a. m.
 *
 */

package com.alp.app.ui.main.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.alp.app.R
import com.alp.app.data.model.UpdateInfoModel
import com.alp.app.databinding.FragmentProfileDetailsBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.view.activities.HomeActivity
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment() {

    private val binding get() = _binding!!
    private var image : String = ""
    private var bitmap: Bitmap? = null
    private val codeCapturePhoto = 2
    private val selectPhotoPermission = 3
    private val args: ProfileDetailsFragmentArgs by navArgs()
    private var _binding: FragmentProfileDetailsBinding? = null
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var contexto: Context
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        functions = Functions(contexto)
        with(binding){
            if(args.imagen.isEmpty()){
                Glide.with(contexto).load(R.drawable.ic_baseline_account_circle_24).into(binding.image)
            } else {
                Picasso.get()
                    .load(args.imagen)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(image)
            }
            iENames.setText(args.nombres)
            iEPassword.isEnabled = false
            iELastNames.setText(args.apellidos)
            iEPassword.setText(args.claveAcceso)
            iEEmail.setText(args.correoElectronico)
            sounds.isChecked = PreferencesSingleton.read("enabled_sound" , false)
            changeTheme.isChecked = PreferencesSingleton.read("mode_dark" , false)
            btnUploadImage.setOnClickListener  { alertaSubirImagen() }
            btnLogout.setOnClickListener {
                PreferencesSingleton.delete("active_session")
                startActivity(Intent(contexto, HomeActivity::class.java))
                activity?.finish()
            }
            iLPassword.setEndIconOnClickListener {  Navigation.findNavController(it).navigate(R.id.action_profileDetailsFragment_to_changePasswordFragment) }
            sounds.setOnCheckedChangeListener { _, isChecked ->
                when(isChecked){
                    true ->  PreferencesSingleton.write("enabled_sound", true)
                    false -> PreferencesSingleton.write("enabled_sound", false)
                }
            }
            when (args.idnotificaciones) {
                "1" -> notifications.isChecked = true
                "2" -> notifications.isChecked = false
            }
            changeTheme.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    PreferencesSingleton.write("mode_dark", true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    PreferencesSingleton.write("mode_dark", false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupShowData() {
        val names = binding.iENames.text.toString()
        if (bitmap!=null) { image = convertImageToBase64(bitmap!!) }
        val lastNames = binding.iELastNames.text.toString()
        val email = binding.iEEmail.text.toString()
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.setInfoProfile(idUser , names, image, lastNames, email).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun renderList(data: Response<UpdateInfoModel>) {
        val response = data.body()!!
        if (response.response == 1) {
            DynamicToast.makeSuccess(contexto, getString(R.string.text_updated_data), Toast.LENGTH_LONG).show()
        } else {
            DynamicToast.makeError(contexto, getString(R.string.text_error_data), Toast.LENGTH_LONG).show()
        }
    }

    private fun alertaSubirImagen() {
        val options = arrayOf(resources.getString(R.string.text_capture_image), resources.getString(R.string.text_choose_image), resources.getString(R.string.text_cancel))
        MaterialAlertDialogBuilder(contexto)
                .setTitle(resources.getString(R.string.text_option_choose))
                .setCancelable(false)
                .setItems(options) { dialog, position ->
                    when(options[position]){
                        resources.getString(R.string.text_capture_image)   -> openCamera()
                        resources.getString(R.string.text_choose_image)  -> loadGallery()
                        resources.getString(R.string.text_cancel)     -> dialog.dismiss()
                    }
                }
                .show()
    }

    private fun convertImageToBase64(bitmap: Bitmap): String {
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
            resultSelectPhoto.launch(intent)
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(contexto.applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsCamera()
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            captureResult.launch(intent)
        }
    }

    private fun requestPermissionsGallery() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DynamicToast.makeError(contexto, getString(R.string.text_enable_permissions), Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), selectPhotoPermission)
        }
    }

    private fun requestPermissionsCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
            DynamicToast.makeSuccess(contexto, getString(R.string.text_enable_permissions), Toast.LENGTH_LONG).show()
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

    private var resultSelectPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data: Intent? = it.data
            val imageSelected = data!!.data
            try {
                imageSelected?.let {
                    if(Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(contexto.contentResolver, imageSelected)
                        binding.image.setImageBitmap(bitmap)
                    } else {
                        val source = ImageDecoder.createSource(contexto.contentResolver, imageSelected)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        binding.image.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private var captureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data: Intent? = it.data
            try {
                bitmap = data!!.extras?.get("data") as Bitmap
                binding.image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                if (bitmap!=null) binding.image.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.save -> setupShowData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

}