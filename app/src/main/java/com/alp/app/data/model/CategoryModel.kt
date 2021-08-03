/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 12:21 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CategoryModel (
        @SerializedName("id_category") var id_category : Int,
        @SerializedName("title")       var title       : String,
        @SerializedName("description") var description : String,
        @SerializedName("image")       var image       : String,
        @SerializedName("created_at")  var created_at  : String
)