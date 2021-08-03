/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 16/07/21, 1:53 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class RepliesModel(
    @SerializedName("id_reply") var id_reply: Int,
    @SerializedName("id_user")  var id_user: Int,
    @SerializedName("reply") var reply: String,
    @SerializedName("full_name") var full_name: String,
    @SerializedName("image") var image: String,
    @SerializedName("likes")           var likes: Int,
    @SerializedName("active_my_like")  var active_my_like: Int,
    @SerializedName("date_created") var date_created: String,
    @SerializedName("date_updated")    var date_updated: String
)