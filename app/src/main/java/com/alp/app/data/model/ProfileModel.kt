/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 6/06/21 03:46 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @SerializedName("respuesta")         var respuesta: String,
    @SerializedName("imagen")            var imagen:String,
    @SerializedName("nombres")           var nombres:String,
    @SerializedName("apellidos")         var apellidos:String,
    @SerializedName("idnotificaciones")  var notificaciones:String,
    @SerializedName("correoElectronico") var correo:String,
    @SerializedName("claveAcceso")       var clave:String,
)