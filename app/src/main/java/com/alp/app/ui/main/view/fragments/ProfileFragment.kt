/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 3/07/21 09:18 PM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.ProfileModel
import com.alp.app.databinding.FragmentProfileBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private var imagen:String? = null
    private var nombres:String? = null
    private var apellidos:String? = null
    private var notificaciones:String? = null
    private var correo:String? = null
    private var clave:String? = null
    private lateinit var functions: Functions
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        functions = Functions(contexto)
        functions.showHideProgressBar(true, binding.progress)
        binding.btnMyCertificates.setOnClickListener {
            findNavController().navigate(R.id.accion_perfil_a_diplomas)
        }
        setupShowData()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupShowData() {
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.getInfoProfile(idUser!!).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: Response<ProfileModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            if (response.imagen.isEmpty()){
                Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(binding.image)
            } else {
                Picasso.get()
                    .load(response.imagen)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.image)
            }
            val nombre = "${response.nombres} ${response.apellidos}"
            binding.fullName.text = nombre
            imagen = response.imagen
            nombres = response.nombres
            apellidos = response.apellidos
            notificaciones = response.notificaciones
            correo = response.correo
            clave = response.clave
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_profile -> {
                if(nombres != null){
                    val action = ProfileFragmentDirections.accionPerfilAPerfilConfiguracion(imagen!!, nombres!!, apellidos!!, notificaciones!!, correo!!, clave!!)
                    findNavController().navigate(action)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}