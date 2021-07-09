/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 12:48 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CoursesModel (
        @SerializedName("id_course")  var id_course  : Int,
        @SerializedName("title")      var title      : String,
        @SerializedName("image")      var image      : String,
        @SerializedName("created_at") var created_at : String
)