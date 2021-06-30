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
import com.alp.app.databinding.TemplateCoursesHomeBinding
import com.alp.app.ui.main.view.fragments.HomeFragmentDirections
import com.bumptech.glide.Glide
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CoursesHomeAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<CoursesHomeAdapter.ViewHolder>() {

    var list = ArrayList<CoursesModel>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TemplateCoursesHomeBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_courses_home, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        with(holder.binding){
            Picasso.get()
                    .load(list.image)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageCourse)
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
