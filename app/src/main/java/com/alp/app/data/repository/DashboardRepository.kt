/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 10:40 PM
 *
 */

package com.alp.app.data.repository

import com.alp.app.data.api.ApiService
import com.alp.app.data.model.*
import retrofit2.Response
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCategories() : List<CategoryModel> {
        return apiService.getCategories()
    }

    suspend fun getSlider() : List<SliderModel> {
        return apiService.getSlider()
    }

    suspend fun getDetailTopic(id_topic: Int, id_user: Int) : List<DetailTopicModel> {
        return apiService.getDetailTopic(id_topic, id_user)
    }

    suspend fun getDetailTopicFavorite(id_detail_topic: Int) : Response<DetailTopicFavoriteModel> {
        return apiService.getDetailTopicFavorite(id_detail_topic)
    }

    suspend fun insertVisit(id_detail_topic: Int) : Response<InsertVisitModel> {
        return apiService.insertVisit(id_detail_topic)
    }

    suspend fun getComments(id_user: Int, id_detail_topic: Int) : List<CommentsCourseModel> {
        return apiService.getComments(id_user, id_detail_topic)
    }

    suspend fun insertProgress(id_user: Int, id_detail_topic: Int, id_topic: Int) : Response<InsertProgressModel> {
        return apiService.insertProgress(id_user, id_detail_topic, id_topic)
    }

    suspend fun surveyTopic(satisfaction: Int, id_topic: Int) : Response<SurveyModel> {
        return apiService.surveyTopic(satisfaction, id_topic)
    }

    suspend fun sendReportDetailTopic(report: String, comment: String, id_detail_topic: Int, id_user: Int) : Response<ReportDetailTopicModel> {
        return apiService.sendReportDetailTopic(report, comment, id_detail_topic, id_user)
    }

    suspend fun sendReportComment(report: String, comment: String, id_comment: Int, id_user: Int) : Response<ReportDetailTopicModel> {
        return apiService.sendReportComment(report, comment, id_comment, id_user)
    }

    suspend fun sendReportReply(report: String, comment: String, id_reply: Int, id_user: Int) : Response<ReportDetailTopicModel> {
        return apiService.sendReportReply(report, comment, id_reply, id_user)
    }

    suspend fun editComment(id_comment: Int, comment: String) : Response<EditCommentModel> {
        return apiService.editComment(id_comment, comment)
    }

    suspend fun editReply(id_reply: Int, reply: String) : Response<EditReplyModel> {
        return apiService.editReply(id_reply, reply)
    }

    suspend fun deleteComment(id_comment: Int) : Response<DeleteCommentModel> {
        return apiService.deleteComment(id_comment)
    }

    suspend fun deleteReply(id_reply: Int) : Response<DeleteReplyModel> {
        return apiService.deleteReply(id_reply)
    }

    suspend fun changeLike(active: Int, id_comment: Int, id_user: Int) : Response<ChangeLikeModel> {
        return apiService.changeLike(active, id_comment, id_user)
    }

    suspend fun changeFavorite(active: Int, id_detail_topic: Int, id_user: Int, id_course: Int) : Response<ChangeFavoriteModel> {
        return apiService.changeFavorite(active, id_detail_topic, id_user, id_course)
    }

    suspend fun changeLikeReply(active: Int, id_comment: Int, id_user: Int) : Response<ChangeLikeModel> {
        return apiService.changeLikeReply(active, id_comment, id_user)
    }


    suspend fun getReplies(id_user: Int, id_comment: Int) : List<RepliesModel> {
        return apiService.getReplies(id_user, id_comment)
    }

    suspend fun searchCourses(search: String) : List<CoursesModel> {
        return apiService.searchCourses(search)
    }

    suspend fun getCourses(id_category: Int) : List<CoursesModel> {
        return apiService.getCourses(id_category)
    }

    suspend fun getCoursesHome() : List<CoursesModel> {
        return apiService.getCoursesHome()
    }

    suspend fun setResetPassword(password: String) : Response<ResetPasswordModel> {
        return apiService.setResetPassword(password)
    }

    suspend fun getInfoProfile(id_user: Int) : Response<ProfileModel> {
        return apiService.getInfoProfile(id_user)
    }

    suspend fun getFavorites(id_user: Int) : List<FavoritesModel> {
        return apiService.getFavorites(id_user)
    }

    suspend fun addComment(id_user: Int, id_detail_topic: Int, comment: String) : Response<AddCommentModel> {
        return apiService.addComment(id_user, id_detail_topic, comment)
    }

    suspend fun addReply(id_user: Int, id_comment: Int, comment: String) : Response<AddReplyModel> {
        return apiService.addReply(id_user, id_comment, comment)
    }

    suspend fun setInfoProfile(id_user: Int, names: String, image: String, last_names: String, email: String) : Response<UpdateInfoModel> {
        return apiService.setInfoProfile(id_user, names, image, last_names, email)
    }

    suspend fun setSignIn(email: String, password: String) : Response<SignInModel> {
        return apiService.setSignIn(email, password)
    }

    suspend fun tokenUser(id_user: Int, token: String) : Response<InsertTokenModel> {
        return apiService.tokenUser(id_user, token)
    }

    suspend fun generateCertificate(id_user : Int ,id_course: Int) : Response<InsertCertificateModel> {
        return apiService.generateCertificate(id_user, id_course)
    }

    suspend fun certificateDetail(id_user : Int ,id_course: Int) : Response<CertificateDetailModel> {
        return apiService.certificateDetail(id_user, id_course)
    }

    suspend fun setSignUp(names: String, last_names: String, email: String, password: String) : Response<SignUpModel> {
        return apiService.setSignUp(names,last_names,email,password)
    }

    suspend fun setPassword(current_password: String, new_password: String, id_user: Int) : Response<UpdatePasswordModel> {
        return apiService.setPassword(current_password, new_password , id_user)
    }

    suspend fun getCertificate(id_user: Int) : List<CertificateModel> {
        return apiService.getCertificate(id_user)
    }

    suspend fun getTopics(id_course: Int, id_user: Int) : List<TopicsModel> {
        return apiService.getTopics(id_course, id_user)
    }
}