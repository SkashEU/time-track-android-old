package com.skash.timetrack.core.di

import com.skash.timetrack.core.repository.ApiAuthRepository
import com.skash.timetrack.core.repository.ApiProjectRepository
import com.skash.timetrack.core.repository.AuthRepository
import com.skash.timetrack.core.repository.ProjectRepository
import com.skash.timetrack.core.repository.ProjectTimeRepository
import com.skash.timetrack.core.repository.RealmProjectTimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
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

    @Provides
    @Singleton
    fun provideProjectTimeRepository(
        scheduler: Scheduler
    ): ProjectTimeRepository {
        return RealmProjectTimeRepository(scheduler)
    }

    @Provides
    fun provideBackgroundScheduler(): Scheduler {
        return Schedulers.io()
    }
}