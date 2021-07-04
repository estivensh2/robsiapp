/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 9/06/21 12:40 AM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class UpdatePasswordModel (
        @SerializedName("data") var data: String,
)