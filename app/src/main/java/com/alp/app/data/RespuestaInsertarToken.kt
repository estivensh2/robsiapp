package com.alp.app.data

import com.google.gson.annotations.SerializedName

data class RespuestaInsertarToken (
    @SerializedName("respuesta") var respuesta: String,
)