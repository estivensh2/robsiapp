/*
 * *
 *  * Created by estiv on 14/07/21, 2:11 a. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 2:11 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class RepliesModel(
    @SerializedName("id_reply") var id_reply: Int,
    @SerializedName("reply") var reply: String,
    @SerializedName("full_name") var full_name: String,
    @SerializedName("image") var image: String,
    @SerializedName("date_created") var date_created: String,
)