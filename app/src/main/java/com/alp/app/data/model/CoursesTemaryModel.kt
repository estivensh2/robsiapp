/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 02:01 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CoursesTemaryModel (
    @SerializedName("id")           var id_temary: Int,
    @SerializedName("idcurso")      var idcurso:Int,
    @SerializedName("nombre")       var nombre:String,
    @SerializedName("descripcion")  var descripcion:String,
    @SerializedName("codigo")       var codigo:String,
    @SerializedName("tipolenguaje") var tipolenguaje:String,
    @SerializedName("imgresultado") var imgresultado:String,
    @SerializedName("total")        var total:String,
    @SerializedName("habilitado")   var habilitado:String
    )