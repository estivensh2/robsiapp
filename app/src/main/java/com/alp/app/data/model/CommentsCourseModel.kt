/*
 * *
 *  * Created by estiv on 13/07/21 07:07 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 13/07/21 07:07 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CommentsCourseModel(
        @SerializedName("id_comment")   var id_comment: Int,
        @SerializedName("comment")      var comment: String,
        @SerializedName("full_name")    var full_name: String,
        @SerializedName("image")        var image: String,
        @SerializedName("replies")      var replies: Int,
        @SerializedName("likes")        var likes: Int,
        @SerializedName("date_created") var date_created: String
)