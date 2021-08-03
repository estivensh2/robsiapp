/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 15/07/21, 6:28 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CommentsCourseModel(
        @SerializedName("id_comment")      var id_comment: Int,
        @SerializedName("id_user")         var id_user: Int,
        @SerializedName("comment")         var comment: String,
        @SerializedName("full_name")       var full_name: String,
        @SerializedName("image")           var image: String,
        @SerializedName("replies")         var replies: Int,
        @SerializedName("likes")           var likes: Int,
        @SerializedName("active_my_like")  var active_my_like: Int,
        @SerializedName("date_created")    var date_created: String,
        @SerializedName("date_updated")    var date_updated: String
)