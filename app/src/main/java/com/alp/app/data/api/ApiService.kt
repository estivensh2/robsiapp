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

    @GET("certificates")
    suspend fun getCertificate(
            @Query("id_user") id_user : Int
    ): List<CertificateModel>

    @GET("detail-topics")
    suspend fun getDetailTopic(
            @Query("id_topic") id_topic : Int,
            @Query("id_user")  id_user: Int
    ): List<DetailTopicModel>

    @GET("detail-topic-favorite")
    suspend fun getDetailTopicFavorite(
        @Query("id_detail_topic") id_detail_topic : Int
    ): Response<DetailTopicFavoriteModel>

    @GET("comments")
    suspend fun getComments(
            @Query("id_user")         id_user : Int,
            @Query("id_detail_topic") id_detail_topic : Int
    ): List<CommentsCourseModel>

    @FormUrlEncoded
    @POST("insert-visit")
    suspend fun insertVisit(
        @Field("id_detail_topic") id_detail_topic : Int,
    ): Response<InsertVisitModel>

    @FormUrlEncoded
    @POST("progress")
    suspend fun insertProgress(
        @Field("id_user")         id_user         : Int,
        @Field("id_detail_topic") id_detail_topic : Int,
        @Field("id_topic")        id_topic        : Int
    ): Response<InsertProgressModel>

    @FormUrlEncoded
    @POST("survey")
    suspend fun surveyTopic(
        @Field("satisfaction")    satisfaction : Int,
        @Field("id_topic")        id_topic     : Int
    ): Response<SurveyModel>

    @FormUrlEncoded
    @POST("report-detail-topic")
    suspend fun sendReportDetailTopic(
        @Field("report")           report           : String,
        @Field("comment")          comment          : String,
        @Field("id_detail_topic")  id_detail_topic  : Int,
        @Field("id_user")          id_user          : Int
    ): Response<ReportDetailTopicModel>

    @FormUrlEncoded
    @POST("report-comment")
    suspend fun sendReportComment(
        @Field("report")           report           : String,
        @Field("comment")          comment          : String,
        @Field("id_comment")  id_comment  : Int,
        @Field("id_user")          id_user          : Int
    ): Response<ReportDetailTopicModel>

    @FormUrlEncoded
    @POST("report-reply")
    suspend fun sendReportReply(
        @Field("report")           report           : String,
        @Field("comment")          comment          : String,
        @Field("id_reply")  id_reply  : Int,
        @Field("id_user")          id_user          : Int
    ): Response<ReportDetailTopicModel>

    @FormUrlEncoded
    @PUT("comments/{id_comment}")
    suspend fun editComment(
        @Path("id_comment") id_comment : Int,
        @Field("comment")   comment    : String
    ): Response<EditCommentModel>

    @FormUrlEncoded
    @PUT("replies/{id_reply}")
    suspend fun editReply(
        @Path("id_reply") id_reply : Int,
        @Field("reply")   reply    : String
    ): Response<EditReplyModel>

    @DELETE("comments/{id_comment}")
    suspend fun deleteComment(
        @Path("id_comment") id_comment :Int,
    ): Response<DeleteCommentModel>

    @DELETE("replies/{id_reply}")
    suspend fun deleteReply(
        @Path("id_reply") id_reply :Int,
    ): Response<DeleteReplyModel>

    @GET("replies")
    suspend fun getReplies(
        @Query("id_user") id_user : Int,
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


    @GET("favorites")
    suspend fun getFavorites (
        @Query("id_user") id_user: Int
    ) : List<FavoritesModel>

    @FormUrlEncoded
    @POST("comments")
    suspend fun addComment (
        @Field("id_user") id_user: Int,
        @Field("id_detail_topic") id_detail_topic: Int,
        @Field("comment") comment: String
    ) : Response<AddCommentModel>

    @FormUrlEncoded
    @POST("replies")
    suspend fun addReply (
        @Field("id_user") id_user: Int,
        @Field("id_comment") id_comment: Int,
        @Field("comment") comment: String
    ) : Response<AddReplyModel>

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
    @POST("favorites")
    suspend fun changeFavorite (
        @Field("active")          active :Int,
        @Field("id_detail_topic") id_detail_topic:Int,
        @Field("id_user")         id_user:Int,
        @Field("id_course")       id_course:Int
    ) : Response<ChangeFavoriteModel>

    @FormUrlEncoded
    @POST("change-like-reply")
    suspend fun changeLikeReply (
        @Field("active")    active   : Int,
        @Field("id_reply")  id_reply : Int,
        @Field("id_user")   id_user  : Int
    ) : Response<ChangeLikeModel>

    @FormUrlEncoded
    @POST("login")
    suspend fun setSignIn (
            @Field("email")    email:String,
            @Field("password") password:String
    ) : Response<SignInModel>

    @FormUrlEncoded
    @PUT("token-user/{id_user}")
    suspend fun tokenUser (
        @Path("id_user") id_user : Int,
        @Field("token")  token   : String
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
    @POST("certificates")
    suspend fun generateCertificate (
            @Field("id_user")   id_user   : Int,
            @Field("id_course") id_course : Int,
    ) : Response<InsertCertificateModel>

    @FormUrlEncoded
    @POST("certificate-detail")
    suspend fun certificateDetail (
        @Field("id_user")   id_user   : Int,
        @Field("id_course") id_course : Int,
    ) : Response<CertificateDetailModel>
}