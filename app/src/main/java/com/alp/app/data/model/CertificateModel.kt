package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

class CertificateModel (
    @SerializedName("data")         var data: String,
    @SerializedName("nombres")      var nombrescurso: String,
    @SerializedName("apellidos")    var apellidoscurso: String,
    @SerializedName("nombrecurso")  var nombrecurso: String,
    @SerializedName("imagen")       var imagen: String,
    @SerializedName("urldescarga")  var urldescarga: String,
)