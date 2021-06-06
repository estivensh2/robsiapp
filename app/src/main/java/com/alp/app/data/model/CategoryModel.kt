package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CategoryModel (
    @SerializedName("id")           var id: String,
    @SerializedName("nombre")       var nombre:String,
    @SerializedName("descripcion")  var descripcion:String,
    @SerializedName("icono")        var icono:String,
    @SerializedName("nuevo")        var nuevo:String,
)