/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/08/21, 3:22 p. m.
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
import com.alp.app.data.model.InsertProgressModel
import com.alp.app.databinding.FragmentDetailTopicBinding
import com.alp.app.databinding.TemplateFinishTopicBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.DetailTopicAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

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
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
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

    private fun setupUI() {
        with(binding){
            detailTopicAdapter  = DetailTopicAdapter(fragmentManager = childFragmentManager, lifecycle = viewLifecycleOwner.lifecycle, args.idCourse)
            viewPager2.apply {
                adapter = detailTopicAdapter
                isUserInputEnabled = false
            }
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.view.isClickable = false
                when(position % 2){
                    0 -> tab.icon = ContextCompat.getDrawable(contexto, R.drawable.ic_baseline_article_24)
                    1 -> tab.icon = ContextCompat.getDrawable(contexto, R.drawable.ic_sharp_help_24)
                }
            }.attach()
        }
    }

    private fun setupShowData() {
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.getDetailTopic(args.idTopic, idUser).observe(requireActivity()) { response ->
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

    private fun insertProgress(id_detail_topic: Int?) {
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.insertProgress(idUser, id_detail_topic!!, args.idTopic).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        //functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderProgress(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        //functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        //functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun renderProgress(data: Response<InsertProgressModel>) {
        if (data.body()!!.response == 1){
            Log.i("response", "1")
        } else {
            Log.i("response", "2")
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

    override fun onButtonClicked(index: Int?, id_detail_topic: Int?) {
        if (index != null) {
            insertProgress(id_detail_topic)
            binding.viewPager2.currentItem = index + 1
        }
    }

    private fun showTopicFinish(){
        val dialog = BottomSheetDialog(contexto)
        val binding = TemplateFinishTopicBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(binding.root)
        binding.btnSkip.setOnClickListener {
            dialog.dismiss()
            val action = DetailTopicFragmentDirections.actionDetailTopicFragmentToTopicsFragment(
                args.idCourse,
                args.nameCourse,
                args.imageCourse
            )
            findNavController().navigate(action)
        }
        var radioButton: RadioButton? = null
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.btnSubmit.isEnabled = group.checkedRadioButtonId != -1
            radioButton = group.findViewById(checkedId)
        }
        binding.btnSubmit.setOnClickListener {
            surveyTopic(radioButton?.hint.toString().toInt())
            dialog.dismiss()
            val action = DetailTopicFragmentDirections.actionDetailTopicFragmentToTopicsFragment(
                args.idCourse,
                args.nameCourse,
                args.imageCourse
            )
            findNavController().navigate(action)
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun surveyTopic(satisfaction: Int?) {
        dashboardViewModel.surveyTopic(satisfaction!!, args.idTopic).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { data ->
                            if (data.body()!!.response == 1){
                                Log.d("Survey", "Inserted")
                            } else {
                                Log.d("Survey", "Failed")
                            }
                        }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        //functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        //functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    override fun onButtonClicked(index: Int?) {
        if (index != null) {
            if(index == detailTopicAdapter.list.size-1){
                showTopicFinish()
            } else {
                binding.viewPager2.currentItem = index + 1
            }
        }
    }
}