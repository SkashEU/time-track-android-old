package com.skash.timetrack.core.di

import android.content.Context
import com.skash.timetrack.core.repository.ApiAuthRepository
import com.skash.timetrack.core.repository.ApiClientRepository
import com.skash.timetrack.core.repository.ApiProjectRepository
import com.skash.timetrack.core.repository.ApiTeamRepository
import com.skash.timetrack.core.repository.AuthRepository
import com.skash.timetrack.core.repository.ClientRepository
import com.skash.timetrack.core.repository.ProfileSectionRepository
import com.skash.timetrack.core.repository.ProjectColorRepository
import com.skash.timetrack.core.repository.ProjectRepository
import com.skash.timetrack.core.repository.RealmProjectColorRepository
import com.skash.timetrack.core.repository.RealmTaskRepository
import com.skash.timetrack.core.repository.RealmWorkTimeRepository
import com.skash.timetrack.core.repository.SharedPrefsProfileSectionRepository
import com.skash.timetrack.core.repository.TaskRepository
import com.skash.timetrack.core.repository.TeamRepository
import com.skash.timetrack.core.repository.WorkTimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @Singleton
    fun provideProfileSectionRepository(
        @ApplicationContext
        context: Context
    ): ProfileSectionRepository {
        return SharedPrefsProfileSectionRepository(context)
    }

    @Provides
    @Singleton
    fun provideTeamRepository(): TeamRepository {
        return ApiTeamRepository()
    }

    @Provides
    @Singleton
    fun provideClientRepository(): ClientRepository {
        return ApiClientRepository()
    }

    @Provides
    fun provideBackgroundScheduler(): Scheduler {
        return Schedulers.io()
    }
}