package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class RespuestaInsertarToken (
    @SerializedName("respuesta") var respuesta: String,
)