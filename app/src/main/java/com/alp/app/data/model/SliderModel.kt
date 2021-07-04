/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/06/21 05:59 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class SliderModel (
        @SerializedName("title")       var title       : String,
        @SerializedName("description") var description : String,
        @SerializedName("image")       var image       : String,
)