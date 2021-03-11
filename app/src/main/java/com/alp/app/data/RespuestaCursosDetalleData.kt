package com.alp.app.data

import com.google.gson.annotations.SerializedName

data class RespuestaCursosDetalleData (
    @SerializedName("id")           var id: String,
    @SerializedName("idcurso")      var idcurso:String,
    @SerializedName("nombre")       var nombre:String,
    @SerializedName("descripcion")  var descripcion:String,
    @SerializedName("codigo")       var codigo:String,
    @SerializedName("tipolenguaje") var tipolenguaje:String,
    @SerializedName("total")        var total:String,
    @SerializedName("habilitado")   var habilitado:String
    )