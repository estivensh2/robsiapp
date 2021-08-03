/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 12:21 a. m.
 *
 */

/*
 * EHOALAELEALAE
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CoursesTemaryDetailsModel(
    @SerializedName("data") var data : String,
    @SerializedName("name") var name : String,
    @SerializedName("description") var description : String,
    @SerializedName("code") var code : String,
    @SerializedName("type_language") var type_language : String,
    @SerializedName("image") var image : String,
    @SerializedName("total") var total : Int
)