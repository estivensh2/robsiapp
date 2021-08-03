/*
 * *
 *  * Created by estiv on 14/07/21, 1:27 a. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 1:27 a. m.
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.ChangeLikeModel
import com.alp.app.data.model.DeleteReplyModel
import com.alp.app.data.model.RepliesModel
import com.alp.app.data.model.ReportDetailTopicModel
import com.alp.app.databinding.FragmentRepliesBinding
import com.alp.app.databinding.TemplateReportCommentBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.RepliesAdapter
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
class RepliesFragment : Fragment() {

    private var _binding: FragmentRepliesBinding? = null
    private val binding get() = _binding!!
    private val args: RepliesFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var repliesAdapter: RepliesAdapter
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRepliesBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        functions = Functions(contexto)
        setupUI()
        setupShowData()
        with(binding){
            fullNameProfile.text = args.fullNameComment
            comment.text = args.comment
            date.text = args.dateComment
            if (args.imageComment.isNullOrEmpty()){
                Glide.with(contexto).load(R.drawable.ic_baseline_account_circle_24).into(binding.imageProfile)
            } else {
                Picasso.get()
                    .load(args.imageComment)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(binding.imageProfile)
            }
            addReply.setOnClickListener {
                val action = RepliesFragmentDirections.actionRepliesFragmentToAddReplyFragment(
                    args.idComment,
                    args.dateComment,
                    args.comment,
                    args.fullNameComment,
                    args.imageComment
                )
                it.findNavController().navigate(action)
            }
            swipeRefreshLayout.apply {
                setOnRefreshListener { setupShowData() }
            }
        }
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            val dividerItemDecoration = DividerItemDecoration(contexto, LinearLayoutManager.VERTICAL)
            repliesAdapter  = RepliesAdapter(
                object : RepliesAdapter.ItemClickListener{
                    override fun itemClick(data: RepliesModel, position: Int, view: View) {
                        val idUser = PreferencesSingleton.read("id_user", 0)
                        if (data.id_user == idUser){
                            val popupMenu = PopupMenu(contexto, view)
                            popupMenu.inflate(R.menu.config_comment)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                popupMenu.gravity = Gravity.END
                            }
                            popupMenu.setOnMenuItemClickListener {
                                when(it.itemId){
                                    R.id.edit_comment -> {
                                        val action = RepliesFragmentDirections.actionRepliesFragmentToEditReplyFragment(
                                            data.id_reply,
                                            data.reply,
                                            args.idComment,
                                            args.fullNameComment,
                                            args.imageComment,
                                            args.comment,
                                            args.dateComment
                                        )

                                        findNavController().navigate(action)
                                    }
                                    R.id.delete_comment -> {
                                        deleteComment(data.id_reply, position)
                                    }
                                }
                                true
                            }
                            popupMenu.show()
                        } else {
                            val popupMenu = PopupMenu(contexto, view)
                            popupMenu.inflate(R.menu.report_comment)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                popupMenu.gravity = Gravity.END
                            }
                            popupMenu.setOnMenuItemClickListener {
                                when(it.itemId){
                                    R.id.report_comment -> {
                                        showDialog(data)
                                    }
                                }
                                true
                            }
                            popupMenu.show()
                        }
                    }

                },
                object: RepliesAdapter.LikeClickListener{
                    override fun itemClick(data: RepliesModel, boolean: Boolean) {
                        if (boolean){
                            changeLike(1, data)
                        } else {
                            changeLike(0, data)
                        }
                    }
                })
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && binding.addReply.isShown){
                        binding.addReply.hide()
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        binding.addReply.show()
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
            recyclerView.layoutManager = LinearLayoutManager(contexto)
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.adapter = repliesAdapter
        }
    }

    private fun showDialog(data: RepliesModel) {
        val dialog = AlertDialog.Builder(contexto)
        val binding = TemplateReportCommentBinding.inflate(layoutInflater, null, false)
        dialog.setView(binding.root)
        val alertDialog = dialog.create()
        binding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.btnReport.isEnabled = false
        binding.radioGroup.setOnCheckedChangeListener { group, _ ->
            binding.btnReport.isEnabled = group.checkedRadioButtonId != -1
        }
        var radioButton: RadioButton? = null
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.btnReport.isEnabled = group.checkedRadioButtonId != -1
            radioButton = group.findViewById(checkedId)
        }
        binding.btnReport.setOnClickListener {
            sendReport(radioButton?.text.toString(), binding.iEMessageReport.text.toString(), data.id_reply, alertDialog, binding)
        }
        alertDialog.show()
    }

    private fun sendReport(report: String, comment: String, id_reply: Int, alertDialog: AlertDialog, binding: TemplateReportCommentBinding) {
        val idUser = PreferencesSingleton.read("id_user", 0)!!
        dashboardViewModel.sendReportReply(report, comment, id_reply, idUser).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> insertReport(data,alertDialog) }
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

    private fun insertReport(data: Response<ReportDetailTopicModel>, alertDialog: AlertDialog) {
        if (data.body()!!.response == 1){
            alertDialog.dismiss()
            DynamicToast.makeSuccess(contexto, getString(R.string.text_send_report), Toast.LENGTH_LONG).show()
        } else {
            DynamicToast.makeError(contexto, getString(R.string.text_error_send), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteComment(id_comment: Int, position: Int) {
        dashboardViewModel.deleteReply(id_comment).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { data -> deleteItem(data, position) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        //
                    }
                }
            }
        }
    }

    private fun changeLike(active: Int, data: RepliesModel) {
        val idUser = PreferencesSingleton.read("id_user", 0)!!
        dashboardViewModel.changeLikeReply(active, data.id_reply, idUser).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { data -> successLike(data) }
                    }
                    Status.ERROR -> {
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        //
                    }
                }
            }
        }
    }


    private fun successLike(data: Response<ChangeLikeModel>) {
        if (data.body()!!.response == 1){
            Log.i("response", getString(R.string.text_inserted))
        } else {
            Log.i("response", getString(R.string.text_updated))
        }
    }

    private fun deleteItem(data: Response<DeleteReplyModel>, position: Int) {
        if (data.body()!!.response == 1){
            repliesAdapter.list.removeAt(position)
            repliesAdapter.notifyItemRemoved(position)
        }
    }

    private fun setupShowData() {
        val idUser = PreferencesSingleton.read("id_user", 0)!!
        dashboardViewModel.getReplies(idUser,args.idComment).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.recyclerView.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                        functions.showHideProgressBar(false, binding.progress)
                    }
                    Status.LOADING -> {
                        binding.recyclerView.visibility = View.GONE
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        }
    }

    private fun renderList(data: List<RepliesModel>) {
        if (data.isNotEmpty()){
            repliesAdapter.apply {
                updateData(data)
                notifyDataSetChanged()
            }
        } else {
            binding.textNoFound.apply {
                visibility = View.VISIBLE
                text = resources.getString(R.string.text_no_found)
            }
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