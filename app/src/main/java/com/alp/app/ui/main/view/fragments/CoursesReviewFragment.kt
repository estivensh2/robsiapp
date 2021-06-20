package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.alp.app.data.model.ReviewModel
import com.alp.app.databinding.FragmentCoursesReviewBinding
import com.alp.app.ui.main.adapter.ReviewAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.material.tabs.TabLayoutMediator
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class CoursesReviewFragment : Fragment() {

    private var _binding: FragmentCoursesReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var functions : Functions
    @Inject
    lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoursesReviewBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        val bundle = this.arguments
        if (bundle!= null) {
            val idcursox = bundle.getString("idcursox", "no")
            Toast.makeText(contexto, idcursox, Toast.LENGTH_SHORT).show()
        }
        val route = arrayOf("PYTHON","HTML", "CSS", "JAVASCRIPT")
        for(i in route) {
            val radioButton = RadioButton(contexto)
            radioButton.text = i
            binding.radio.addView(radioButton)
        }
        binding.validar.setOnClickListener { 
            val obtener = binding.radio.checkedRadioButtonId
            Toast.makeText(contexto, "$obtener", Toast.LENGTH_SHORT).show()
            if (obtener==3) {
                Toast.makeText(contexto, "si", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(contexto, "error", Toast.LENGTH_SHORT).show()
            }

        }
        setupUI()
        //getReview()
        return binding.root
    }


    private fun setupUI() {
        with(binding){
            reviewAdapter  = ReviewAdapter()
            viewPager2.apply {
                adapter = reviewAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        //indicator.selection = position
                    }
                })
            }
        }
    }

    private fun getReview() {
        dashboardViewModel.getReview(1).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        //resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        functions.showHideProgressBar(false, binding.progress)
                        DynamicToast.makeError(contexto, response.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }


    private fun renderList(data: Response<ReviewModel>) {
        reviewAdapter.apply {
            //updateData(data)
        }
       // binding.indicator.tabCount = reviewAdapter.list.size
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