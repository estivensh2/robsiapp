package com.alp.app.servicios

import com.alp.app.data.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIServicio {

    @FormUrlEncoded
    @POST("recuperarDatosUsuario.php")
    fun recuperarDatosUsuario(@Field("id") id:String) : Call<RespuestaUsuarioData>

    @FormUrlEncoded
    @POST("registrarUsuario.php")
    fun registrarUsuario (
            @Field("nombres") nombres:String,
            @Field("apellidos") apellidos:String,
            @Field("correoElectronico") correo:String,
            @Field("claveAcceso") clave:String)
    : Call<RespuestaCrearCuentaData>

    @FormUrlEncoded
    @POST("iniciarSesion.php")
    fun iniciarSesion(
        @Field("correoElectronico") correo:String,
        @Field("claveAcceso") clave:String)
    : Call<RespuestaIniciarSesionData>

    @POST("recuperarSlider.php")
    fun recuperarSliderx() : Call<List<RespuestaSliderData>>

    @POST("recuperarCategorias.php")
    fun recuperarCategorias() : Call<List<RespuestaCategoriaData>>

    @FormUrlEncoded
    @POST("recuperarDatosCursos.php")
    fun recuperarCursos(@Field("idcategoria") idcategoria:String)
    : Call<List<RespuestaCursosData>>

    @FormUrlEncoded
    @POST("recuperarDatosCursosDetalle.php")
    fun recuperarCursosDetalle(@Field("idcurso") idcurso:String)
    : Call<List<RespuestaCursosDetalleData>>

    @FormUrlEncoded
    @POST("insertarHabilitado.php")
    fun insertarProgreso(
        @Field("idhabilitado") idhabilitado:String,
        @Field("idcursodetalle") idcursodetalle:String,
        @Field("idcurso") idcurso:String)
    : Call<RespuestaInsertarProgreso>


    @FormUrlEncoded
    @POST("cambiarClave.php")
    fun cambiarClave(
            @Field("claveAccesoActual") claveactual:String,
            @Field("claveAccesoNuevaConfirmada") actualconfirmada:String,
            @Field("idusuario") idusuario:String)
            : Call<RespuestaCambiarClave>
}