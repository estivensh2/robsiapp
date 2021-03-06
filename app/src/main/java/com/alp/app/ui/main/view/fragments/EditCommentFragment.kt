/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/08/21, 9:40 p. m.
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
import androidx.navigation.fragment.navArgs
import com.alp.app.R
import com.alp.app.data.model.CommentsCourseModel
import com.alp.app.data.model.EditCommentModel
import com.alp.app.databinding.FragmentCommentsCourseBinding
import com.alp.app.databinding.FragmentEditCommentBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.CommentsCourseAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response


@AndroidEntryPoint
class EditCommentFragment : Fragment() {

    private var _binding: FragmentEditCommentBinding? = null
    private val binding get() = _binding!!
    private val args: EditCommentFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditCommentBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        setHasOptionsMenu(true)
        binding.iEComment.setText(args.comment)
        return binding.root
    }

    private fun setupShowData() {
        dashboardViewModel.editComment(args.idComment, binding.iEComment.text.toString()).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun renderList(data: Response<EditCommentModel>) {
        val response = data.body()!!
        if (response.response == 1) {
            DynamicToast.makeSuccess(contexto, resources.getString(R.string.text_update_comment), Toast.LENGTH_LONG).show()
            val idUser = PreferencesSingleton.read("id_user", 0)
            val action = EditCommentFragmentDirections.actionEditCommentFragmentToCommentsCourseFragment(
                idUser,
                args.idDetailTopic
            )
            findNavController().navigate(action)
        } else {
            DynamicToast.makeError(contexto, "HA OCURRIDO UN ERROR", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_comment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_comment -> {
                setupShowData()
            }
        }
        return super.onOptionsItemSelected(item)
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