/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 01:33 AM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class SignInModel(
        @SerializedName("response")  var response: Int,
        @SerializedName("id_user")   var id_user: Int,
)