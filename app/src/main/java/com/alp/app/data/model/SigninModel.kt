package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class SigninModel(
    @SerializedName("respuesta")         var respuesta: String,
    @SerializedName("id")                var id: String,
    @SerializedName("imagen")            var imagen:String,
    @SerializedName("nombres")           var nombres:String,
    @SerializedName("apellidos")         var apellidos:String,
    @SerializedName("idnotificaciones")  var notificaciones:String,
    @SerializedName("correoElectronico") var correo:String,
    @SerializedName("claveAcceso")       var clave:String,
)