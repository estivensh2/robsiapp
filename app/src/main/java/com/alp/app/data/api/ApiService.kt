/**
 * Copyright desde 2021 "Tu Barberia" y colaboradores
 *
 * DESCARGO DE RESPONSABILIDAD
 *
 * No edite ni agregue a este archivo si desea actualizar o personalizar este repositorio para sus
 * necesidades, consulte https://derechos.tubarberia.co para obtener más información.
 *
 * @author "Tu Barberia"
 * Colaboradores:
 *            GitHub
 *            @estivensh4 Estiven Sanchez <estivensh4@gmail.com>
 *            @bedoyastok Daniel Bedoya <bedoya.stok@gmail.com>
 *            @andresgimenex Andres Jimenez <andresjimenez593@gmail.com>
 * @copyright Desde 2021 "Tu Barberia" y colaboradores
 */

package com.alp.app.data.api

import com.alp.app.data.model.*
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("recuperarCategorias.php")
    suspend fun getCategories(): List<CategoryModel>

    @POST("recuperarDatosExamen.php")
    suspend fun getReview(): List<CategoryModel>

    @POST("recuperarSlider.php")
    suspend fun getSlider(): List<SliderModel>

    @FormUrlEncoded
    @POST("recuperarDatosCursos.php")
    suspend fun getCourses(
            @Field("idcategoria") id_category : String
    ): List<CoursesModel>

    @FormUrlEncoded
    @POST("recuperarDiploma.php")
    suspend fun getCertificate(
            @Field("idusuario") idusuario:String
    ): List<CertificateModel>



    @FormUrlEncoded
    @POST("recuperarDatosCursosDetalle.php")
    suspend fun getCoursesTemary(
            @Field("idcurso")   idcurso   : String,
            @Field("idusuario") idusuario : String
    ): List<CoursesTemaryModel>


    @FormUrlEncoded
    @POST("recuperarDatosUsuario.php")
    suspend fun getInfoProfile (
            @Field("id") id:String
    ) : Response<ProfileModel>

    @FormUrlEncoded
    @POST("actualizarDatosUsuario.php")
    suspend fun setInfoProfile (
            @Field("id") id:String,
            @Field("nombres") nombres:String,
            @Field("imagen") imagen:String,
            @Field("apellidos") apellidos:String,
            @Field("correoElectronico") correo:String
    ) : Response<UpdateInfoModel>

    @FormUrlEncoded
    @POST("cambiarClave.php")
    suspend fun setPassword (
            @Field("claveAccesoActual") claveactual:String,
            @Field("claveAccesoNuevaConfirmada") actualconfirmada:String,
            @Field("idusuario") idusuario:String
    ) : Response<UpdatePasswordModel>

    @FormUrlEncoded
    @POST("iniciarSesion.php")
    suspend fun setSignIn (
            @Field("correoElectronico") correo:String,
            @Field("claveAcceso") clave:String
    ) : Response<SigninModel>

    @FormUrlEncoded
    @POST("insertarOActualizarToken.php")
    suspend fun setToken (
            @Field("idusuario") idusuario:String,
            @Field("idtoken") idtoken:String
    ) : Response<InsertTokenModel>



    @FormUrlEncoded
    @POST("registrarUsuario.php")
    suspend fun setSignUp (
            @Field("nombres") nombres:String,
            @Field("apellidos") apellidos:String,
            @Field("correoElectronico") correo:String,
            @Field("claveAcceso") clave:String
    ) : Response<SignUpModel>

    @FormUrlEncoded
    @POST("recuperarClave.php")
    suspend fun setResetPassword (
            @Field("correoElectronico") correo:String
    ) : Response<ResetPasswordModel>

    @FormUrlEncoded
    @POST("insertarDiploma.php")
    suspend fun setCertificate (
            @Field("idusuario") idusuario:String,
            @Field("idcurso") idcurso:String,
            @Field("idcompletado") idcompletado:String
    ) : Response<InsertCertificateModel>


    @FormUrlEncoded
    @POST("insertarHabilitado.php")
    suspend fun setProgress (
            @Field("idhabilitado") idhabilitado:String,
            @Field("idcursodetalle") idcursodetalle:String,
            @Field("idcurso") idcurso:String,
            @Field("idusuario") idusuario:String
    ) : Response<InsertProgressModel>




}