/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/08/21, 5:36 p. m.
 *
 */

package com.alp.app.ui.main.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.alp.app.data.repository.DashboardRepository
import com.alp.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class DashboardViewModel @ViewModelInject constructor(private val dashboardRepository: DashboardRepository) : ViewModel() {

    fun getCategories() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getCategories()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getSlider() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getSlider()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getDetailTopic(id_topic: Int, id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getDetailTopic(id_topic, id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getDetailTopicFavorite(id_detail_topic: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getDetailTopicFavorite(id_detail_topic)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun insertVisit(id_detail_topic: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.insertVisit(id_detail_topic)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun sendReportDetailTopic(report: String, comment: String, id_detail_topic: Int, id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.sendReportDetailTopic(report, comment, id_detail_topic, id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun sendReportComment(report: String, comment: String, id_comment: Int, id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.sendReportComment(report, comment, id_comment, id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun sendReportReply(report: String, comment: String, id_detail_topic: Int, id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.sendReportReply(report, comment, id_detail_topic, id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun insertProgress(id_user: Int, id_detail_topic: Int, id_topic: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.insertProgress(id_user, id_detail_topic, id_topic)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun surveyTopic(satisfaction: Int, id_topic: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.surveyTopic(satisfaction,id_topic)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getComments(id_user: Int, id_detail_topic: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getComments(id_user, id_detail_topic)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun editComment(id_comment: Int, comment: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.editComment(id_comment, comment)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun editReply(id_reply: Int, reply: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.editReply(id_reply, reply)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun deleteComment(id_comment: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.deleteComment(id_comment)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun deleteReply(id_reply: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.deleteReply(id_reply)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun changeLike(active: Int, id_comment: Int, id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.changeLike(active, id_comment, id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun changeFavorite(active: Int, id_detail_topic: Int, id_user: Int, id_course: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.changeFavorite(active, id_detail_topic, id_user, id_course)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun changeLikeReply(active: Int, id_comment: Int, id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.changeLikeReply(active, id_comment, id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getReplies(id_user: Int, id_comment: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getReplies(id_user, id_comment)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }


    fun searchCourses(search: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.searchCourses(search)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getCourses(id_category: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getCourses(id_category)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getCoursesHome() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getCoursesHome()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun setResetPassword(password: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setResetPassword(password)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun setPassword(current_password: String, new_password: String, id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setPassword(current_password, new_password, id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun setSignIn(email: String, password: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setSignIn(email, password)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun tokenUser(id_user: Int, token: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.tokenUser(id_user, token)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun generateCertificate(id_user : Int ,id_course: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.generateCertificate(id_user, id_course)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun setSignUp(names: String, last_names: String, email: String, password: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setSignUp(names,last_names,email,password)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getFavorites(id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getFavorites(id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun getInfoProfile(id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getInfoProfile(id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun addComment(id_user: Int, id_detail_topic: Int, comment: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.addComment(id_user, id_detail_topic, comment)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun addReply(id_user: Int, id_comment: Int, comment: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.addReply(id_user, id_comment, comment)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun setInfoProfile(id_user: Int, name: String, image: String, last_names: String, email: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setInfoProfile(id_user, name, image, last_names, email)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }


    fun getCertificate(id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getCertificate(id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }


    fun getTopics(id_course: Int, id_user: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getTopics(id_course, id_user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }

    fun certificateDetail(id_user : Int ,id_course: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.certificateDetail(id_user, id_course)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error has occurred"))
        }
    }
}