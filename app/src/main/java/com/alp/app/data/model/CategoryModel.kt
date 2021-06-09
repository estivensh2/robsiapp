package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CategoryModel (
        @SerializedName("id_category") var id_category : String,
        @SerializedName("name")        var name        : String,
        @SerializedName("description") var description : String,
        @SerializedName("icon")        var icon        : String,
        @SerializedName("new")         var new         : String,
)