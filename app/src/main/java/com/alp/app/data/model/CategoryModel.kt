/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 02:38 AM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CategoryModel (
        @SerializedName("id_category") var id_category : Int,
        @SerializedName("name")        var name        : String,
        @SerializedName("description") var description : String,
        @SerializedName("icon")        var icon        : String,
        @SerializedName("new")         var new         : String,
)