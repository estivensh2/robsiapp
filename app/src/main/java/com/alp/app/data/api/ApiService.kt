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

    @POST("get_categories.php")
    suspend fun getCategories(): List<CategoryModel>

    //@POST("get_review.php")
    //suspend fun getReview(): List<CategoryModel>

    @POST("get_slider.php")
    suspend fun getSlider(): List<SliderModel>

    @FormUrlEncoded
    @POST("get_courses.php")
    suspend fun getCourses(
            @Field("id_category") id_category : String
    ): List<CoursesModel>

    @FormUrlEncoded
    @POST("get_certificate.php")
    suspend fun getCertificate(
            @Field("id_user") id_user:String
    ): List<CertificateModel>

    @FormUrlEncoded
    @POST("get_courses_temary.php")
    suspend fun getCoursesTemary(
            @Field("idcurso")   idcurso   : String,
            @Field("idusuario") idusuario : String
    ): List<CoursesTemaryModel>

    @FormUrlEncoded
    @POST("get_user_data.php")
    suspend fun getInfoProfile (
            @Field("id_user") id:String
    ) : Response<ProfileModel>

    @FormUrlEncoded
    @POST("update_user_data.php")
    suspend fun setInfoProfile (
            @Field("id") id:String,
            @Field("nombres") nombres:String,
            @Field("imagen") imagen:String,
            @Field("apellidos") apellidos:String,
            @Field("correoElectronico") correo:String
    ) : Response<UpdateInfoModel>

    @FormUrlEncoded
    @POST("change_password.php")
    suspend fun setPassword (
            @Field("current_password") current_password:String,
            @Field("new_password")     new_password:String,
            @Field("id_user")          id_user:String
    ) : Response<UpdatePasswordModel>

    @FormUrlEncoded
    @POST("sign_in.php")
    suspend fun setSignIn (
            @Field("email")    email:String,
            @Field("password") password:String
    ) : Response<SigninModel>

    @FormUrlEncoded
    @POST("set_token.php")
    suspend fun setToken (
            @Field("id_user") id_user:String,
            @Field("token") token:String
    ) : Response<InsertTokenModel>


    @FormUrlEncoded
    @POST("sign_up.php")
    suspend fun setSignUp (
            @Field("names") names:String,
            @Field("last_names") last_names:String,
            @Field("email") email:String,
            @Field("password") password:String
    ) : Response<SignUpModel>

    @FormUrlEncoded
    @POST("reset_password.php")
    suspend fun setResetPassword (
            @Field("email") email:String
    ) : Response<ResetPasswordModel>

    @FormUrlEncoded
    @POST("insert_certificate.php")
    suspend fun setCertificate (
            @Field("id_user") id_user:String,
            @Field("id_course") id_course:String,
            @Field("id_completed") id_completed:String
    ) : Response<InsertCertificateModel>

    @FormUrlEncoded
    @POST("insert_progress.php")
    suspend fun setProgress (
            @Field("id_enabled") id_enabled:String,
            @Field("id_details_course") id_details_course:String,
            @Field("id_course") id_course:String,
            @Field("id_user") id_user:String
    ) : Response<InsertProgressModel>

}