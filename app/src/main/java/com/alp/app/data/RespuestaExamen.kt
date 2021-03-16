package com.alp.app.data

import com.google.gson.annotations.SerializedName

data class RespuestaExamen (
    @SerializedName("preguntas") var datos: String
    )