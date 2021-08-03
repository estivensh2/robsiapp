/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 24/07/21, 10:53 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class FavoritesModel(
    @SerializedName("id_detail_topic")    val id_detail_topic: Int,
    @SerializedName("title_course")       val title_course: String,
    @SerializedName("title_detail_topic") val title_detail_topic: String,
)