package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CoursesModel (
    @SerializedName("id")     var id: String,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("imagen") var imagen:String,
    @SerializedName("idnuevo") var nuevo:String
)