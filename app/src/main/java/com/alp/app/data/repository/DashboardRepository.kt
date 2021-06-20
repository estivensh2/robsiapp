package com.alp.app.data.repository

import com.alp.app.data.api.ApiService
import com.alp.app.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCategories() : List<CategoryModel> {
        return apiService.getCategories()
    }

    suspend fun getSlider() : List<SliderModel> {
        return apiService.getSlider()
    }

    suspend fun getCourses(id: String) : List<CoursesModel> {
        return apiService.getCourses(id)
    }

    suspend fun setResetPassword(clave: String) : Response<ResetPasswordModel> {
        return apiService.setResetPassword(clave)
    }

    suspend fun getInfoProfile(id: String) : Response<ProfileModel> {
        return apiService.getInfoProfile(id)
    }
    suspend fun setInfoProfile(id: String, nombres: String, imagen: String, apellidos: String, correo: String) : Response<UpdateInfoModel> {
        return apiService.setInfoProfile(id, nombres, imagen, apellidos, correo)
    }

    suspend fun setSignIn(correo: String, clave: String) : Response<SigninModel> {
        return apiService.setSignIn(correo, clave)
    }

    suspend fun setToken(idusuario: String, idtoken: String) : Response<InsertTokenModel> {
        return apiService.setToken(idusuario, idtoken)
    }

    suspend fun setCertificate(idusuario : String ,idcurso: Int, idcompletado: String) : Response<InsertCertificateModel> {
        return apiService.setCertificate(idusuario, idcurso, idcompletado)
    }

    suspend fun getReview(id_course: Int) : Response<ReviewModel> {
        return apiService.getReview(id_course)
    }

    suspend fun setProgress(id_enabled : Int ,id_course_temary: Int, id_course: Int, id_user: String) : Response<InsertProgressModel> {
        return apiService.setProgress(id_enabled, id_course_temary,id_course,id_user)
    }

    suspend fun getDetailsTemary(id_course_temary : Int, id_course: Int, id_user: String) : Response<CoursesTemaryDetailsModel> {
        return apiService.getDetailsTemary(id_course_temary, id_course, id_user)
    }

    suspend fun setSignUp(nombres: String, apellidos: String, correo: String, clave: String) : Response<SignUpModel> {
        return apiService.setSignUp(nombres,apellidos,correo,clave)
    }


    suspend fun setPassword(claveactual: String, clavenueva: String, idusuario: String) : Response<UpdatePasswordModel> {
        return apiService.setPassword(claveactual, clavenueva , idusuario)
    }

    suspend fun getCertificate(id: String) : List<CertificateModel> {
        return apiService.getCertificate(id)
    }

    suspend fun getCoursesTemary(id: String, idusuario: String) : List<CoursesTemaryModel> {
        return apiService.getCoursesTemary(id, idusuario)
    }
}