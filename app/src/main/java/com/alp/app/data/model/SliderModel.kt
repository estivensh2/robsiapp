package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class SliderModel (
    @SerializedName("id")          var id: String,
    @SerializedName("titulo")      var titulo:String,
    @SerializedName("descripcion") var descripcion:String,
    @SerializedName("imagen")      var imagen:String,
)