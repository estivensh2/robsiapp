package com.alp.app.data

import com.google.gson.annotations.SerializedName

data class RespuestaSliderData (
    @SerializedName("id")          var id: String,
    @SerializedName("titulo")      var titulo:String,
    @SerializedName("descripcion") var descripcion:String,
    @SerializedName("imagen")      var imagen:String,
)