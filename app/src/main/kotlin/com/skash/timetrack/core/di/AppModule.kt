package com.skash.timetrack.core.di

import com.skash.timetrack.core.repository.ApiAuthRepository
import com.skash.timetrack.core.repository.ApiProjectRepository
import com.skash.timetrack.core.repository.AuthRepository
import com.skash.timetrack.core.repository.ProjectColorRepository
import com.skash.timetrack.core.repository.ProjectRepository
import com.skash.timetrack.core.repository.TaskRepository
import com.skash.timetrack.core.repository.RealmProjectColorRepository
import com.skash.timetrack.core.repository.RealmTaskRepository
import com.skash.timetrack.core.repository.RealmWorkTimeRepository
import com.skash.timetrack.core.repository.WorkTimeRepository
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
    ): TaskRepository {
        return RealmTaskRepository(scheduler)
    }

    @Provides
    @Singleton
    fun provideProjectColorRepository(
        scheduler: Scheduler
    ): ProjectColorRepository {
        return RealmProjectColorRepository(scheduler)
    }

    @Provides
    @Singleton
    fun provideWorkTimeRepository(
        scheduler: Scheduler
    ): WorkTimeRepository {
        return RealmWorkTimeRepository(scheduler)
    }

    @Provides
    fun provideBackgroundScheduler(): Scheduler {
        return Schedulers.io()
    }
}