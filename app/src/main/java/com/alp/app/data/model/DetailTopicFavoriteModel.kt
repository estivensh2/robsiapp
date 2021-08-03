/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 21/07/21, 7:28 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class DetailTopicFavoriteModel(
    @SerializedName("description") val description : String,
    @SerializedName("level")       val level       : String,
    @SerializedName("visits")      val visits      : Int,
)