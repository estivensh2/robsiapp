package com.alp.app.adaptadores

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alp.app.R
import com.alp.app.data.RespuestaCursosDetalleData
import com.alp.app.databinding.TarjetasCursosDetalleBinding
import com.bumptech.glide.Glide

class CursosDetalleAdaptador (private val arrayList: ArrayList<RespuestaCursosDetalleData>, val contexto: Context): RecyclerView.Adapter<CursosDetalleAdaptador.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bintItems(modelo: RespuestaCursosDetalleData, contexto: Context){
            val binding = TarjetasCursosDetalleBinding.bind(itemView)
            binding.tituloCursox.text = modelo.nombre
            if (position==0){
                modelo.habilitado = "1"
            }
            if (modelo.habilitado == "1"){
                Glide.with(contexto).load(R.drawable.iniciar).into(binding.iconoCursos)
                binding.cardViewCursosDetalle.isEnabled = true
            } else {
                binding.iconoCursos.setBackgroundResource(R.drawable.candado)
                binding.cardViewCursosDetalle.isEnabled = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.tarjetas_cursos_detalle, parent, false)
        return ViewHolder(vista)
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItems(arrayList[position], contexto)
        holder.itemView.setOnClickListener { v ->
            val bundle = Bundle()
            val modelo = arrayList[position]
            val id = modelo.id
            val idcurso = modelo.idcurso
            val nombre = modelo.nombre
            val habil = modelo.habilitado
            val descripcion = modelo.descripcion
            val codigo = modelo.codigo
            val tipolenguaje = modelo.tipolenguaje
            bundle.putString("id", id)
            bundle.putString("idcurso", idcurso)
            bundle.putString("nombre", nombre)
            bundle.putString("habilitado", habil)
            bundle.putString("descripcion", descripcion)
            bundle.putString("codigo", codigo)
            bundle.putString("tipolenguaje", tipolenguaje)
            bundle.putString("total", arrayList.size.toString())
            bundle.putString("ultimoelemento", arrayList.last().total)
            Navigation.findNavController(v).navigate(R.id.accion_detalle_a_temario, bundle)
        }
    }

}