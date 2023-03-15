package com.skash.timetrack.core.di

import com.skash.timetrack.core.repository.ApiAuthRepository
import com.skash.timetrack.core.repository.ApiProjectRepository
import com.skash.timetrack.core.repository.AuthRepository
import com.skash.timetrack.core.repository.ProjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return ApiAuthRepository()
    }

    @Provides
    @Singleton
    fun provideProjectRepository(): ProjectRepository {
        return ApiProjectRepository()
    }
}