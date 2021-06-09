package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class SliderModel (
        @SerializedName("title")       var title       : String,
        @SerializedName("description") var description : String,
        @SerializedName("image")       var image       : String,
)