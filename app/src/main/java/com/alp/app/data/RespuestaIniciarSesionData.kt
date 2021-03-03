package com.alp.app.data

import com.google.gson.annotations.SerializedName

data class RespuestaIniciarSesionData(
    @SerializedName("respuesta") var respuesta: String,
    @SerializedName("datos") var datos: DatosUsuarioData
)
data class DatosUsuarioData (
    @SerializedName("id")                var id: String,
    @SerializedName("imagen")            var imagen:String,
    @SerializedName("nombres")           var nombres:String,
    @SerializedName("apellidos")         var apellidos:String,
    @SerializedName("idnotificaciones")  var notificaciones:String,
    @SerializedName("correoElectronico") var correo:String,
    @SerializedName("claveAcceso")       var clave:String,
)