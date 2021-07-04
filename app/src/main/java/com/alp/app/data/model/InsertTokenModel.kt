/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 9/06/21 01:11 AM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class InsertTokenModel (
        @SerializedName("data") var data: String,
)