package com.alp.app.ui.main.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.model.CertificateModel
import com.alp.app.databinding.TemplateCertificatesBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CertificateAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<CertificateAdapter.ViewHolder>() {
    // obtenemos la lista de datos
    var list = ArrayList<CertificateModel>()
    // creamos la clase para mostrar los campos en la vista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TemplateCertificatesBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_certificates, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        with(holder.binding){
            tituloCursoDiploma.text = list.nombrecurso
            iconoDescargar.setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(list.urldescarga)))
            }
            Glide.with(context).load(list.imagen).into(iconoCursosx)
        }
    }

    fun updateData(data: List<CertificateModel>) {
        this.list.apply {
            clear()
            addAll(data)
        }
    }
}