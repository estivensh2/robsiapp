package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.data.model.CategoryModel
import com.alp.app.data.model.CoursesModel
import com.alp.app.data.model.SliderModel
import com.alp.app.databinding.FragmentHomeBinding
import com.alp.app.ui.main.adapter.CategoriesAdapter
import com.alp.app.ui.main.adapter.CoursesHomeAdapter
import com.alp.app.ui.main.adapter.SliderAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.gms.ads.AdRequest
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var contexto: Context
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var functions: Functions
    private val dashboardViewModel: DashboardViewModel by viewModels()
    @Inject
    lateinit var adapterCategories: CategoriesAdapter
    private lateinit var adapterSlider: SliderAdapter
    private lateinit var adapterCourses: CoursesHomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        functions.showHideProgressBar(true, binding.progress)
        setupUI()
        setupShowData()
        initLoadAds()
        binding.viewProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_profile2)
        }
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            adapterCategories = CategoriesAdapter(contexto)
            adapterSlider     = SliderAdapter(contexto)
            adapterCourses    = CoursesHomeAdapter(contexto)
            recyclerViewSlider.adapter     = adapterSlider
            recyclerViewCourses.adapter    = adapterCourses
            recyclerViewCategories.adapter = adapterCategories
            recyclerViewCategories.layoutManager = LinearLayoutManager(contexto)
            recyclerViewSlider.layoutManager     = LinearLayoutManager(contexto, LinearLayoutManager.HORIZONTAL, true)
            recyclerViewCourses.layoutManager    = LinearLayoutManager(contexto, LinearLayoutManager.HORIZONTAL, true)
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getCategories().observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.recyclerViewCategories.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        binding.recyclerViewCategories.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerViewCategories.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
        dashboardViewModel.getSlider().observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.recyclerViewSlider.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> retrieveSlider(data) }
                    }
                    Status.ERROR   -> {
                        binding.recyclerViewSlider.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerViewSlider.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
        dashboardViewModel.getCoursesHome().observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.recyclerViewCourses.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> retrieveCourses(data) }
                    }
                    Status.ERROR   -> {
                        binding.recyclerViewCourses.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerViewCourses.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun retrieveSlider(list: List<SliderModel>) {
        adapterSlider.apply {
            updateData(list)
            notifyDataSetChanged()
        }
    }

    private fun retrieveCourses(list: List<CoursesModel>) {
        adapterCourses.apply {
            updateData(list)
            notifyDataSetChanged()
        }
    }

    private fun renderList(data: List<CategoryModel>) {
        adapterCategories.apply {
            updateData(data)
            notifyDataSetChanged()
        }
    }

    private fun initLoadAds() {
        val adRequest = AdRequest.Builder().build()
        binding.banner.loadAd(adRequest)
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