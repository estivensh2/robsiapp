/*
 * *
 *  * Created by estiv on 21/07/21, 12:56 a. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 21/07/21, 12:56 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class FavoritesModel(
    @SerializedName("id_detail_topic")    val id_detail_topic: Int,
    @SerializedName("title_course")       val title_course: String,
    @SerializedName("title_detail_topic") val title_detail_topic: String,
)