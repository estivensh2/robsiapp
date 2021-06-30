/*
 * EHOALAELEALAE
 */

package com.alp.app.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CoursesModel
import com.alp.app.databinding.TemplateSearchCoursesBinding
import com.alp.app.ui.main.view.fragments.CoursesFragmentDirections
import com.alp.app.ui.main.view.fragments.HomeFragmentDirections
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SearchCoursesAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<SearchCoursesAdapter.ViewHolder>() {

    var list = ArrayList<CoursesModel>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TemplateSearchCoursesBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_search_courses, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        with(holder.binding){
            titleCourse.text = list.name
            Glide.with(context).load(list.image).into(imageCourse)
        }
        holder.itemView.setOnClickListener {
            val idCourse = list.id_course
            val name = list.name
            val image = list.image
            val action = HomeFragmentDirections.actionHomeFragmentToCoursesTemaryFragment(idCourse, name, image)
            it.findNavController().navigate(action)
        }
    }

    fun updateData(users: List<CoursesModel>) {
        this.list.apply {
            clear()
            addAll(users)
        }
    }
}