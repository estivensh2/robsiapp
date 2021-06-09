package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CoursesModel (
        @SerializedName("id_course") var id_course: String,
        @SerializedName("name")      var name:String,
        @SerializedName("image")     var image:String,
        @SerializedName("new")       var new:String
)