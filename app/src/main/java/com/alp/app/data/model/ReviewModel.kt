/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 20/06/21 04:47 PM
 *
 */

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
