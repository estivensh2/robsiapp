/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 12:21 a. m.
 *
 */

package com.alp.app.data.model

import android.util.ArrayMap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewModel (
        @SerializedName("id_question")  var id_question       : Int,
        @SerializedName("question")    var question : String,
        //@SerializedName("answers")    var answers : List<Answers>,
        @SerializedName("response")    var response : Int,
)
