package com.alp.app.ui.main.views.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.ui.main.views.adapter.CertificateAdapter
import com.alp.app.data.model.CertificateModel
import com.alp.app.databinding.FragmentListadoDiplomadosBinding
import com.alp.app.servicios.PreferencesSingleton
import com.alp.app.ui.main.views.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CertificateFragment : Fragment() {

    private var _binding: FragmentListadoDiplomadosBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    @Inject
    lateinit var certificateAdapter: CertificateAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListadoDiplomadosBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), "preferenciasDeUsuario")
        binding.progress.visibility = View.VISIBLE
        setupUI()
        setupShowData()
        return binding.root
    }


    private fun setupUI() {
        with(binding){
            certificateAdapter  = CertificateAdapter(contexto)
            recycler.layoutManager = LinearLayoutManager(contexto)
            recycler.adapter = certificateAdapter
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getCertificate(PreferencesSingleton.leer("id","0").toString()).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.recycler.visibility = View.VISIBLE
                        showHideProgressBar(false)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        binding.recycler.visibility = View.VISIBLE
                        showMessage(response.message!!)
                        showHideProgressBar(false)
                    }
                    Status.LOADING -> {
                        binding.recycler.visibility = View.GONE
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

    private fun renderList(data: List<CertificateModel>) {
        certificateAdapter.apply {
            updateData(data)
            notifyDataSetChanged()
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