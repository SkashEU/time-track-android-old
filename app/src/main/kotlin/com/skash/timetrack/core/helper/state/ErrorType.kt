package com.skash.timetrack.core.helper.state

import androidx.annotation.StringRes
import com.skash.timetrack.R

sealed class ErrorType(@StringRes val errorMessage: Int) {

    companion object {
        fun fromThrowable(throwable: Throwable): ErrorType? {
            //TODO: Try to parse throwable to error tyspe
            return null
        }
    }

    object UserUnauthenticated : ErrorType(R.string.error_type_user_unauthenticated)
    object ProjectFetch : ErrorType(R.string.error_type_projects_fetch)
    object ProjectTimeSave : ErrorType(R.string.error_type_project_time_create)
    object NoProjectColorSelected : ErrorType(R.string.error_type_project_color_empty)
    object NoProjectTitleSelected : ErrorType(R.string.error_type_project_title_empty)
    object ProjectModify : ErrorType(R.string.error_type_project_modify_failed)
    object TaskFetch : ErrorType(R.string.error_type_tasks_fetch)
    object NoWorkspaceSelected : ErrorType(R.string.error_type_no_workspace_selected)
    object WorkspaceMemberFetch : ErrorType(R.string.error_type_member_fetch)
    object NoOrganizationSelected : ErrorType(R.string.error_type_no_organization_selected)
    object ClientsFetch : ErrorType(R.string.error_type_clients_fetch)
    object WorkspaceFetch : ErrorType(R.string.error_type_workspace_fetch)
    object OrganizationFetch : ErrorType(R.string.error_type_organizations_fetch)
    object UsernameChange : ErrorType(R.string.error_type_username_change)
    object UsernameEmpty : ErrorType(R.string.error_type_username_empty)
    object BackupCodesFetch: ErrorType(R.string.error_type_backup_codes_fetch)
}