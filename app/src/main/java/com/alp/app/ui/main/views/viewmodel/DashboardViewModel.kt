package com.alp.app.ui.main.views.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.alp.app.data.repository.DashboardRepository
import com.alp.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.http.Field
import java.lang.Exception

class DashboardViewModel @ViewModelInject constructor(private val dashboardRepository: DashboardRepository) : ViewModel() {

    fun getCategories() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getCategories()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }

    fun getSlider() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getSlider()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }

    fun getCourses(id: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getCourses(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }

    fun setPassword(claveactual: String, clavenueva: String, idusuario: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setPassword(claveactual, clavenueva, idusuario)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }

    fun setSignIn(correo: String, clave: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setSignIn(correo, clave)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }

    fun setSignUp(nombres: String, apellidos: String, correo: String, clave: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setSignUp(nombres,apellidos,correo,clave)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }



    fun getInfoProfile(id: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getInfoProfile(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }

    fun setInfoProfile(id: String, nombres: String, imagen: String, apellidos: String, correo: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.setInfoProfile(id, nombres, imagen, apellidos, correo)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }


    fun getCertificate(id: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getCertificate(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }


    fun getCoursesTemary(id: String, idusuario: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dashboardRepository.getCoursesTemary(id, idusuario)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ha ocurrido un error"))
        }
    }

}