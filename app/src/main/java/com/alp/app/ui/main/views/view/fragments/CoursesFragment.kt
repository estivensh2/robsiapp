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
import com.alp.app.data.model.CoursesModel
import com.alp.app.databinding.FragmentCoursesBinding
import com.alp.app.ui.main.views.adapter.CoursesAdapter
import com.alp.app.ui.main.views.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private lateinit var contexto: Context
    private lateinit var id : String
    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()
    @Inject
    lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle!= null) {
            val name = bundle.getString("nombre", "no")
            val icon = bundle.getString("icono", "no")
            id = bundle.getString("id", "no")
            binding.tituloCategoria.text = name
            Glide.with(contexto).load(icon).into(binding.iconoCategoria)
        }
        binding.progress.visibility = View.VISIBLE
        setupUI()
        setupShowData()
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            coursesAdapter  = CoursesAdapter(contexto)
            recycler.layoutManager = LinearLayoutManager(contexto)
            recycler.adapter = coursesAdapter
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getCourses(id).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: List<CoursesModel>) {
        coursesAdapter.apply {
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