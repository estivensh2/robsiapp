package com.alp.app.ui.main.views.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.ProfileModel
import com.alp.app.databinding.FragmentProfileBinding
import com.alp.app.servicios.PreferencesSingleton
import com.alp.app.ui.main.views.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
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
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.misCertificados.setOnClickListener {
            findNavController().navigate(R.id.accion_perfil_a_diplomas)
        }
        setupShowData()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupShowData() {
        dashboardViewModel.getInfoProfile(PreferencesSingleton.leer("id","0").toString()).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: Response<ProfileModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            if (response.imagen.isEmpty()){
                Glide.with(contexto).load(R.drawable.usuario).signature(ObjectKey(System.currentTimeMillis())).into(binding.imagenPerfil)
            } else {
                Glide.with(requireContext()).load(response.imagen).signature(ObjectKey(System.currentTimeMillis())).into(binding.imagenPerfil)
            }
            val nombre = "${response.nombres} ${response.apellidos}"
            binding.nombreCompleto.text = nombre
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
        inflater.inflate(R.menu.menu_perfil, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.configuraciones_perfil -> {
                if(nombres != null){
                    val action = ProfileFragmentDirections.accionPerfilAPerfilConfiguracion(imagen!!, nombres!!, apellidos!!, notificaciones!!, correo!!, clave!!)
                    findNavController().navigate(action)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}