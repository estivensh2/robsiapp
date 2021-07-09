/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 6/06/21 03:46 PM
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