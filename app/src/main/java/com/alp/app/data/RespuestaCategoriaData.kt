package com.alp.app.data

import com.google.gson.annotations.SerializedName

data class RespuestaCategoriaData (
    @SerializedName("id")                var id: String,
    @SerializedName("nombre")      var nombre:String,
    @SerializedName("fondo") var fondo:String,
    @SerializedName("descripcion")         var descripcion:String,
    @SerializedName("icono")         var icono:String,
    @SerializedName("nuevo")         var nuevo:String,
)