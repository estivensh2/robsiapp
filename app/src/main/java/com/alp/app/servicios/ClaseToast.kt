package com.alp.app.servicios

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alp.app.R

object ClaseToast {

    fun mostrarx(contexto: Context,mensaje: String, colorFondo: Int, imagenFondo: Int) {
        val inflater: LayoutInflater = LayoutInflater.from(contexto)
        val view = View(contexto)
        val contenido = view.findViewById<ViewGroup>(R.id.vista_personalizada)
        val layout = inflater.inflate(R.layout.vista_toast_personalizada, contenido)
        val texto = layout.findViewById<TextView>(R.id.texto_mensaje)
        val fondo = layout.findViewById<LinearLayout>(R.id.fondo_ventana)
        val imagen = layout.findViewById<ImageView>(R.id.imagen_ventana)
        val mostrarMensaje = Toast.makeText(contexto, "", Toast.LENGTH_SHORT)
            texto.text = mensaje
            fondo.setBackgroundColor(colorFondo)
            imagen.setImageResource(imagenFondo)
            mostrarMensaje.view = layout
            mostrarMensaje.setGravity(Gravity.BOTTOM, 0, 0)
            mostrarMensaje.show()
    }
}