package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class SigninModel(
        @SerializedName("data")         var data: String,
        @SerializedName("id_user")      var id_user: String,
)