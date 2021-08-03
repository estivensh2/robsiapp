/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 1/08/21, 1:54 a. m.
 *
 */

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

package com.alp.app.di

import com.alp.app.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
object NetworkingModule {

    @Provides
    fun providesBaseUrl(): String {
        return "http://192.168.0.18/backendalp/public/api/"
    }

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()

        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.build()
        return okHttpClient.build()
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient, baseUrl: String, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideRestApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}