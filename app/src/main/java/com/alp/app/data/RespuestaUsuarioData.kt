package com.alp.app.data

import com.google.gson.annotations.SerializedName

data class RespuestaUsuarioData(
    @SerializedName("respuesta") var respuesta: String,
    @SerializedName("datos") var datos: DatosUsuarioDatax
)
data class DatosUsuarioDatax (
    @SerializedName("imagen") var imagen:String,
    @SerializedName("nombres") var nombres:String,
    @SerializedName("apellidos") var apellidos:String,
    @SerializedName("idnotificaciones") var notificaciones:String,
    @SerializedName("correoElectronico") var correo:String,
    @SerializedName("claveAcceso") var clave:String,
)