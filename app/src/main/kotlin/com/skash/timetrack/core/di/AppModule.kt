package com.skash.timetrack.core.di

import android.annotation.SuppressLint
import android.content.Context
import com.skash.timetrack.BuildConfig
import com.skash.timetrack.api.network.ApiClient
import com.skash.timetrack.api.network.api.AuthApi
import com.skash.timetrack.api.network.api.ProjectApi
import com.skash.timetrack.api.network.api.TaskApi
import com.skash.timetrack.api.network.api.UserApi
import com.skash.timetrack.api.network.api.WorkspaceApi
import com.skash.timetrack.api.network.api.WorktimeApi
import com.skash.timetrack.core.repository.ApiAuthRepository
import com.skash.timetrack.core.repository.ApiClientRepository
import com.skash.timetrack.core.repository.ApiOrganizationRepository
import com.skash.timetrack.core.repository.ApiProjectRepository
import com.skash.timetrack.core.repository.ApiTaskRepository
import com.skash.timetrack.core.repository.ApiTeamRepository
import com.skash.timetrack.core.repository.ApiUserRepository
import com.skash.timetrack.core.repository.ApiWorkTimeRepository
import com.skash.timetrack.core.repository.ApiWorkspaceRepository
import com.skash.timetrack.core.repository.AuthRepository
import com.skash.timetrack.core.repository.ClientRepository
import com.skash.timetrack.core.repository.OrganizationRepository
import com.skash.timetrack.core.repository.ProfileSectionRepository
import com.skash.timetrack.core.repository.ProjectColorRepository
import com.skash.timetrack.core.repository.ProjectRepository
import com.skash.timetrack.core.repository.RealmProjectColorRepository
import com.skash.timetrack.core.repository.SharedPrefsProfileSectionRepository
import com.skash.timetrack.core.repository.TaskRepository
import com.skash.timetrack.core.repository.TeamRepository
import com.skash.timetrack.core.repository.UserRepository
import com.skash.timetrack.core.repository.WorkTimeRepository
import com.skash.timetrack.core.repository.WorkspaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Ignoring: Vapor Backend cant accept partial minutes
    @SuppressLint("SimpleDateFormat")
    private val apiClient = ApiClient(BuildConfig.BASE_URL).setDateFormat(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    )

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        @ApplicationContext
        context: Context
    ): AuthRepository {
        return ApiAuthRepository(authApi, context)
    }

    @Provides
    @Singleton
    fun provideProjectRepository(
        projectApi: ProjectApi,
        @ApplicationContext
        context: Context
    ): ProjectRepository {
        return ApiProjectRepository(projectApi, context)
    }

    @Provides
    @Singleton
    fun provideProjectTimeRepository(
        userApi: UserApi,
        taskApi: TaskApi,
        @ApplicationContext
        context: Context
    ): TaskRepository {
        return ApiTaskRepository(taskApi, userApi, context)
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
        worktimeApi: WorktimeApi,
        userApi: UserApi,
        @ApplicationContext
        context: Context
    ): WorkTimeRepository {
        return ApiWorkTimeRepository(worktimeApi, userApi, context)
    }

    @Provides
    @Singleton
    fun provideProfileSectionRepository(): ProfileSectionRepository {
        return SharedPrefsProfileSectionRepository()
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
    @Singleton
    fun provideWorkspaceRepository(
        workspaceApi: WorkspaceApi,
        @ApplicationContext
        context: Context
    ): WorkspaceRepository {
        return ApiWorkspaceRepository(workspaceApi, context)
    }

    @Provides
    @Singleton
    fun provideOrganizationRepository(
        userApi: UserApi,
        @ApplicationContext
        context: Context
    ): OrganizationRepository {
        return ApiOrganizationRepository(userApi, context)
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(
        userApi: UserApi,
        @ApplicationContext
        context: Context
    ): UserRepository {
        return ApiUserRepository(userApi, context)
    }

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return apiClient.createService(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        return apiClient.createService(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProjectApi(): ProjectApi {
        return apiClient.createService(ProjectApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWorkspaceApi(): WorkspaceApi {
        return apiClient.createService(WorkspaceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWorktimeApi(): WorktimeApi {
        return apiClient.createService(WorktimeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskApi(): TaskApi {
        return apiClient.createService(TaskApi::class.java)
    }

    @Provides
    fun provideBackgroundScheduler(): Scheduler {
        return Schedulers.io()
    }
}