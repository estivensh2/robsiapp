package com.alp.app.data.model

import android.util.ArrayMap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewModel (
        @SerializedName("id_question")  var id_question       : Int,
        @SerializedName("question")    var question : String,
        @SerializedName("answers")    var answers : List<Answers>,
        @SerializedName("response")    var response : Int,
)
data class Answers(
        @SerializedName("id_option")    var id_option : Int,
        @SerializedName("option")    var option : String,
)
