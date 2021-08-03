/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 12:21 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class ProfileModel(
        @SerializedName("response")      var response      : Int,
        @SerializedName("names")         var names         : String,
        @SerializedName("last_names")    var last_names    : String,
        @SerializedName("image")         var image         : String,
        @SerializedName("email")         var email         : String,
        @SerializedName("password")      var password      : String,
        @SerializedName("notifications") var notifications : Int,
)