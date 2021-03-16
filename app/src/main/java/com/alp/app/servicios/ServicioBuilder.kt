package com.alp.app.servicios

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServicioBuilder {

    private val cliente = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://alpapp.000webhostapp.com/webservices/modulos/webservices/") // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(cliente)
        .build()

    fun<T> buildServicio(service: Class<T>): T{
        return retrofit.create(service)
    }
}