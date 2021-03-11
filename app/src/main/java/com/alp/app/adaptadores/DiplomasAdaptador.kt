package com.alp.app.adaptadores

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.RespuestaRecuperarDiploma
import com.alp.app.databinding.TarjetasListaDiplomasBinding
import com.bumptech.glide.Glide


class DiplomasAdaptador(private val arrayList: ArrayList<RespuestaRecuperarDiploma>, val contexto: Context): RecyclerView.Adapter<DiplomasAdaptador.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bintItems(modelo: RespuestaRecuperarDiploma, contexto: Context){
            val binding = TarjetasListaDiplomasBinding.bind(itemView)
            binding.tituloCursoDiploma.text = modelo.nombrecurso
            binding.iconoDescargar.setOnClickListener {
                contexto.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(modelo.urldescarga)))
            }
            Glide.with(contexto).load(modelo.imagen).into(binding.iconoCursosx)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(
            R.layout.tarjetas_lista_diplomas,
            parent,
            false
        )
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItems(arrayList[position], contexto)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}