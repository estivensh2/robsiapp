/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 11:27 PM
 *
 */

package com.alp.app.data.api

import com.alp.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("categories")
    suspend fun getCategories(): List<CategoryModel>

    @FormUrlEncoded
    @POST("get_review.php")
    suspend fun getReview(
            @Field("id_course") id_course : Int
    ): List<ReviewModel>

    @GET("slider")
    suspend fun getSlider(): List<SliderModel>

    @GET("courses")
    suspend fun getCourses(
            @Query("id_category") id_category : Int
    ): List<CoursesModel>

    @GET("courses-home")
    suspend fun getCoursesHome(): List<CoursesModel>

    @GET("search")
    suspend fun searchCourses(
            @Query("search") search : String
    ): List<CoursesModel>

    @FormUrlEncoded
    @POST("certificates")
    suspend fun getCertificate(
            @Field("id_user") id_user : Int
    ): List<CertificateModel>

    @GET("detail-topics")
    suspend fun getDetailTopic(
            @Query("id_topic") id_topic : Int
    ): List<DetailTopicModel>

    @GET("comments")
    suspend fun getComments(
            @Query("id_user") id_user : Int,
            @Query("id_detail_topic") id_detail_topic : Int
    ): List<CommentsCourseModel>

    @FormUrlEncoded
    @PUT("comments/{id_comment}")
    suspend fun editComment(
        @Path("id_comment") id_comment : Int,
        @Field("comment")   comment    : String
    ): Response<EditCommentModel>

    @DELETE("comments/{id_comment}")
    suspend fun deleteComment(
        @Path("id_comment") id_comment :Int,
    ): Response<DeleteCommentModel>

    @GET("replies")
    suspend fun getReplies(
        @Query("id_user") id_topic : Int,
        @Query("id_comment") id_comment : Int
    ): List<RepliesModel>

    @GET("topics")
    suspend fun getTopics(
            @Query("id_course") id_course : Int,
            @Query("id_user")   id_user   : Int
    ): List<TopicsModel>

    @FormUrlEncoded
    @POST("user-info")
    suspend fun getInfoProfile (
            @Field("id_user") id_user: Int
    ) : Response<ProfileModel>

    @FormUrlEncoded
    @POST("comments")
    suspend fun addComment (
        @Field("id_user") id_user: Int,
        @Field("id_detail_topic") id_detail_topic: Int,
        @Field("comment") comment: String
    ) : Response<AddCommentModel>

    @FormUrlEncoded
    @PUT("users/{id_user}")
    suspend fun setInfoProfile (
            @Path("id_user") id_user:Int,
            @Field("names") names :String,
            @Field("image") image:String,
            @Field("last_names") last_names:String,
            @Field("email") email:String
    ) : Response<UpdateInfoModel>

    @FormUrlEncoded
    @POST("change-password")
    suspend fun setPassword (
            @Field("current_password") current_password:String,
            @Field("new_password")     new_password:String,
            @Field("id_user")          id_user:Int
    ) : Response<UpdatePasswordModel>

    @FormUrlEncoded
    @POST("change-like")
    suspend fun changeLike (
        @Field("active")     active :Int,
        @Field("id_comment") id_comment:Int,
        @Field("id_user")    id_user:Int
    ) : Response<ChangeLikeModel>

    @FormUrlEncoded
    @POST("login")
    suspend fun setSignIn (
            @Field("email")    email:String,
            @Field("password") password:String
    ) : Response<SignInModel>

    @FormUrlEncoded
    @POST("set_token.php")
    suspend fun setToken (
            @Field("id_user") id_user:Int,
            @Field("token") token:String
    ) : Response<InsertTokenModel>

    @FormUrlEncoded
    @POST("users")
    suspend fun setSignUp (
            @Field("names") names:String,
            @Field("last_names") last_names:String,
            @Field("email") email:String,
            @Field("password") password:String
    ) : Response<SignUpModel>

    @FormUrlEncoded
    @POST("reset")
    suspend fun setResetPassword (
            @Field("email") email:String
    ) : Response<ResetPasswordModel>

    @FormUrlEncoded
    @POST("insert_certificate.php")
    suspend fun setCertificate (
            @Field("id_user") id_user:Int,
            @Field("id_course") id_course:Int,
            @Field("id_completed") id_completed:String
    ) : Response<InsertCertificateModel>

    @FormUrlEncoded
    @POST("set_progress.php")
    suspend fun setProgress (
            @Field("id_enabled") id_enabled:Int,
            @Field("id_details_course") id_details_course:Int,
            @Field("id_course") id_course:Int,
            @Field("id_user") id_user:Int
    ) : Response<InsertProgressModel>

    @FormUrlEncoded
    @POST("get_courses_temary_details.php")
    suspend fun getDetailsTemary (
        @Field("id_course_temary") id_course_temary :Int,
        @Field("id_course") id_course : Int,
        @Field("id_user") id_user : Int
    ) : Response<CoursesTemaryDetailsModel>
}