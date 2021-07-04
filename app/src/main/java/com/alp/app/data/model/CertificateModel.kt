/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/06/21 05:33 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

class CertificateModel (
    @SerializedName("data")         var data: Int,
    @SerializedName("nombres")      var nombrescurso: String,
    @SerializedName("apellidos")    var apellidoscurso: String,
    @SerializedName("nombrecurso")  var nombrecurso: String,
    @SerializedName("imagen")       var imagen: String,
    @SerializedName("urldescarga")  var urldescarga: String,
)