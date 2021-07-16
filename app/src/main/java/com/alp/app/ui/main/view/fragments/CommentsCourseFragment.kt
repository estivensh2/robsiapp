/*
 * *
 *  * Created by estiv on 12/07/21 02:07 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 12/07/21 02:07 AM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alp.app.R
import com.alp.app.data.model.ChangeLikeModel
import com.alp.app.data.model.CommentsCourseModel
import com.alp.app.data.model.DeleteCommentModel
import com.alp.app.databinding.FragmentCommentsCourseBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.adapter.CommentsCourseAdapter
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class CommentsCourseFragment : Fragment() {

    private var _binding: FragmentCommentsCourseBinding? = null
    private val binding get() = _binding!!
    //private val args: CommentsCourseFragmentArgs by navArgs()
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var commentsCourseAdapter: CommentsCourseAdapter
    private lateinit var functions: Functions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCommentsCourseBinding.inflate(inflater, container, false)
        PreferencesSingleton.init(requireContext(), resources.getString(R.string.name_preferences))
        functions = Functions(contexto)
        setupUI()
        setupShowData()
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener { setupShowData() }
        }
        binding.addComment.setOnClickListener {
            it.findNavController().navigate(R.id.action_commentsCourseFragment_to_addCommentFragment)
        }
        return binding.root
    }

    private fun setupUI() {
        with(binding){
            val dividerItemDecoration = DividerItemDecoration(contexto, LinearLayoutManager.VERTICAL)
            commentsCourseAdapter = CommentsCourseAdapter(object : CommentsCourseAdapter.ItemClickListener{
                override fun itemClick(data: CommentsCourseModel, position: Int, view: View) {
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
                                    val action = CommentsCourseFragmentDirections.actionCommentsCourseFragmentToEditCommentFragment(data.comment, data.id_comment)
                                    findNavController().navigate(action)
                                }
                                R.id.delete_comment -> {
                                    deleteComment(data.id_comment, position)
                                    Toast.makeText(contexto, "${it.title} $position", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(contexto, "${it.title} $position", Toast.LENGTH_SHORT).show()
                                }
                            }
                            true
                        }
                        popupMenu.show()
                    }
                }

            },
                object: CommentsCourseAdapter.LikeClickListener{
                    override fun itemClick(data: CommentsCourseModel, boolean: Boolean) {
                        if (boolean){
                            changeLike(1, data)
                        } else {
                            changeLike(0, data)
                        }
                    }
                })
            recyclerView.layoutManager = LinearLayoutManager(contexto)
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.adapter = commentsCourseAdapter
        }
    }

    private fun setupShowData() {
        val idUser = PreferencesSingleton.read("id_user", 0)!!
        dashboardViewModel.getComments(idUser,1).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.swipeRefreshLayout.isRefreshing = false
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

    private fun deleteComment(id_comment: Int, position: Int) {
        dashboardViewModel.deleteComment(id_comment).observe(requireActivity()) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> deleteItem(data, position) }
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

    private fun changeLike(active: Int, data: CommentsCourseModel) {
        val idUser = PreferencesSingleton.read("id_user", 0)!!
        dashboardViewModel.changeLike(active, data.id_comment, idUser).observe(requireActivity()) { response ->
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
            Log.i("response", "Se ha insertado")
        } else {
            Log.i("response", "Se ha actualizado")
        }
    }

    private fun deleteItem(data: Response<DeleteCommentModel>, position: Int) {
        if (data.body()!!.response == 1){
            commentsCourseAdapter.list.removeAt(position)
            commentsCourseAdapter.notifyItemRemoved(position)
        }
    }

    private fun renderList(data: List<CommentsCourseModel>) {
        if (data.isNotEmpty()){
            commentsCourseAdapter.apply {
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