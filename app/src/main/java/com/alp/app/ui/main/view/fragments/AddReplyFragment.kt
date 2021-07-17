/*
 * *
 *  * Created by estiv on 15/07/21, 11:03 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 15/07/21, 11:03 p. m.
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
import com.alp.app.data.model.AddReplyModel
import com.alp.app.databinding.FragmentAddReplyBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.bumptech.glide.Glide
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class AddReplyFragment : Fragment() {

    private var _binding: FragmentAddReplyBinding? = null
    private val binding get() = _binding!!
    private val args: AddReplyFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddReplyBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        functions = Functions(contexto)
        setHasOptionsMenu(true)
        with(binding){
            if (args.imageComment.isNullOrEmpty()){
                Glide.with(contexto).load(R.drawable.ic_baseline_account_circle_24).into(binding.imageProfile)
            } else {
                Picasso.get()
                    .load(args.imageComment)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.imageProfile)
            }
            fullNameProfile.text = args.fullnameComment
            date.text = args.dateComment
            comment.text = args.comment
        }
        return binding.root
    }

    private fun setupShowData() {
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.addReply(idUser!!, args.idComment, binding.iEReply.text.toString()).observe(requireActivity(), Observer { response ->
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

    private fun renderList(data: Response<AddReplyModel>) {
        val response = data.body()!!
        if (response.response == 1) {
            val action = AddReplyFragmentDirections.actionAddReplyFragmentToRepliesFragment(
                args.idComment,
                args.fullnameComment,
                args.imageComment,
                args.comment,
                args.dateComment
            )
            DynamicToast.makeSuccess(contexto, resources.getString(R.string.text_success_comment), Toast.LENGTH_LONG).show()
            findNavController().navigate(action)
        } else {
            DynamicToast.makeError(contexto, "HA OCURRIDO UN ERROR", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_reply, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_repply -> {
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