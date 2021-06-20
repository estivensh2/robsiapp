package com.alp.app.data.model

import android.util.ArrayMap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewModel (
    var data: Map<Int, Questions>? = null,
)

data class Questions (
        @SerializedName("question") var question: String,
        @SerializedName("id_question") var id_question: Int
)
