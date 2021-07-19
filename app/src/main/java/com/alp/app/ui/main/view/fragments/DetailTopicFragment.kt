/*
 * *
 *  * Created by estiv on 7/07/21 04:57 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 7/07/21 04:57 PM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alp.app.R
import com.alp.app.data.model.DetailTopicModel
import com.alp.app.databinding.FragmentDetailTopicBinding
import com.alp.app.ui.main.adapter.DetailTopicAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTopicFragment : Fragment(), ItemFragment.OnButtonClickListener, BlankFragment.OnButtonClickListener {

    private var _binding: FragmentDetailTopicBinding? = null
    private val binding get() = _binding!!
    private val args: DetailTopicFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var detailTopicAdapter: DetailTopicAdapter
    var lastClickTime: Long = 0
    private lateinit var functions: Functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = args.nameTopic
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = args.nameTopic
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailTopicBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        setupUI()
        setupShowData()
        setHasOptionsMenu(true)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (lastClickTime.plus(2000L) > System.currentTimeMillis()) {
                    alertDialog()
                } else {
                    DynamicToast.makeWarning(contexto, resources.getString(R.string.text_click_again)).show()
                    lastClickTime = System.currentTimeMillis()
                }
            }
        })
        return binding.root
    }

    private fun alertDialog(){
        MaterialAlertDialogBuilder(contexto)
            .setMessage(resources.getString(R.string.text_cancel_review))
            .setNegativeButton(resources.getString(R.string.text_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.text_yes)){ _, _ ->
                findNavController().navigate(R.id.action_coursesReviewFragment_to_homeFragment)
            }
            .setCancelable(false)
            .show()
    }

    private fun setupUI() {
        with(binding){
            detailTopicAdapter  = DetailTopicAdapter(fragmentManager = childFragmentManager, lifecycle = viewLifecycleOwner.lifecycle)
            viewPager2.apply {
                adapter = detailTopicAdapter
                isUserInputEnabled = false
            }
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                when(position % 2){
                    0 -> tab.icon = ContextCompat.getDrawable(contexto, R.drawable.ic_baseline_article_24)
                    1 -> tab.icon = ContextCompat.getDrawable(contexto, R.drawable.ic_baseline_help_24)
                }
            }.attach()
        }
    }

    private fun setupShowData() {
        dashboardViewModel.getDetailTopic(args.idTopic).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        //binding.viewPager2.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        //binding.viewPager2.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        //binding.viewPager2.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun confirmExit(){
        MaterialAlertDialogBuilder(contexto)
            .setMessage(resources.getString(R.string.text_cancel_review))
            .setNegativeButton(resources.getString(R.string.text_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.text_yes)){ _, _ ->
                val action = DetailTopicFragmentDirections.actionDetailTopicFragmentToTopicsFragment(
                    args.idCourse,
                    args.nameCourse,
                    args.imageCourse
                )
                findNavController().navigate(action)
            }
            .setCancelable(false)
            .show()
    }

    private fun renderList(list: List<DetailTopicModel>) {
        detailTopicAdapter.updateData(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                confirmExit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onButtonClicked(index: Int?) {
        if (index != null) {
            if(index == detailTopicAdapter.list.size-1){
                val action = DetailTopicFragmentDirections.actionDetailTopicFragmentToTopicsFragment(
                    args.idCourse,
                    args.nameCourse,
                    args.imageCourse
                )
                findNavController().navigate(action)
            } else {
                binding.viewPager2.currentItem = index + 1
            }
        }
    }
}