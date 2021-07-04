/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 10/06/21 03:09 AM
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