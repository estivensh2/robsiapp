package com.alp.app.di

import com.alp.app.data.api.ApiService
import com.alp.app.data.repository.DashboardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataRepositoryModule {
    @Provides
    fun provideDataRepository(apiService: ApiService): DashboardRepository {
        return DashboardRepository(apiService)
    }
}