package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class UpdatePasswordModel (
        @SerializedName("data") var data: String,
)