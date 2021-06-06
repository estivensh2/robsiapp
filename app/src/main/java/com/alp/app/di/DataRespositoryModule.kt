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
import com.alp.app.data.repository.DashboardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataRespositoryModule {
    @Provides
    fun provideDataRepository(apiService: ApiService): DashboardRepository {
        return DashboardRepository(apiService)
    }
}