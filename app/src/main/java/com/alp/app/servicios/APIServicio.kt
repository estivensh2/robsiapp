package com.alp.app.servicios

import com.alp.app.data.model.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIServicio {


    @POST("recuperarDatosExamen.php")
    fun recuperarExamen() : Call<List<RespuestaExamen>>

    @FormUrlEncoded
    @POST("insertarHabilitado.php")
    fun insertarProgreso(
        @Field("idhabilitado") idhabilitado:String,
        @Field("idcursodetalle") idcursodetalle:String,
        @Field("idcurso") idcurso:String,
        @Field("idusuario") idusuario:String)
    : Call<RespuestaInsertarProgreso>

    @FormUrlEncoded
    @POST("recuperarClave.php")
    fun recuperarClave(
        @Field("correoElectronico") correo:String
    )
    : Call<RespuestaRecuperarClave>

    @FormUrlEncoded
    @POST("insertarOActualizarToken.php")
    fun insertarToken(
        @Field("idusuario") idusuario:String,
        @Field("idtoken") idtoken:String
    )
    : Call<RespuestaInsertarToken>

    @FormUrlEncoded
    @POST("insertarDiploma.php")
    fun insertarDiploma (
            @Field("idusuario") idusuario:String,
            @Field("idcurso") idcurso:String,
            @Field("idcompletado") idcompletado:String
    )
    : Call<RespuestaInsertarDiploma>
}