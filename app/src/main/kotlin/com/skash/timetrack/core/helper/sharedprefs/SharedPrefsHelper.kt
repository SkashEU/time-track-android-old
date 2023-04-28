package com.skash.timetrack.core.helper.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.skash.timetrack.TimeTrack
import com.skash.timetrack.core.helper.string.toUUID
import com.skash.timetrack.core.model.AuthData
import com.skash.timetrack.core.model.Avatar
import com.skash.timetrack.core.model.Organization
import com.skash.timetrack.core.model.User
import com.skash.timetrack.core.model.Workspace
import java.util.UUID

const val SELECTED_ORGANIZATION_ID = "key_selected_organization_id"
const val SELECTED_WORKSPACE_ID = "key_selected_workspace_id"
const val SELECTED_WORKSPACE_TITLE = "key_selected_workspace_title"
const val USER_EMAIL = "key_user_email"
const val USER_NAME = "key_user_name"

private const val SHARED_PREFS_NAME = "secret_shared_prefs"

private const val SHARED_PREFS_AUTH_DATA = "auth_data"

const val SHARED_PREFS_SELF_USER_ID = "self_user_id"
private const val SHARED_PREFS_SELF_USER_EMAIL = "self_user_email"
private const val SHARED_PREFS_SELF_USER_AVATAR = "self_user_avatar"
private const val SHARED_PREFS_SELF_USER_FIRST_NAME = "self_user_first_name"
private const val SHARED_PREFS_SELF_USER_LAST_NAME = "self_user_last_name"

const val SHARED_PREFS_SELECTED_WORKSPACE_ID = "selected_workspace_id"
private const val SHARED_PREFS_SELECTED_WORKSPACE_TITLE = "selected_workspace_title"
private const val SHARED_PREFS_SELECTED_WORKSPACE_ORGANIZATION_ID =
    "selected_workspace_organization_id"

fun Context.getPrefs(): SharedPreferences {
    return (applicationContext as TimeTrack).sharedPreferences
}

fun SharedPreferences.saveAuthData(authData: AuthData) {
    edit()
        .putString(SHARED_PREFS_AUTH_DATA, authData.bearer)
        .apply()
}

fun SharedPreferences.getAuthData(): AuthData {
    return AuthData(
        getString(SHARED_PREFS_AUTH_DATA, "") ?: ""
    )
}

fun SharedPreferences.saveSelfUser(user: User) {
    edit()
        .putString(SHARED_PREFS_SELF_USER_ID, user.id.toString())
        .putString(SHARED_PREFS_SELF_USER_EMAIL, user.email)
        .putString(SHARED_PREFS_SELF_USER_FIRST_NAME, user.firstName)
        .putString(SHARED_PREFS_SELF_USER_LAST_NAME, user.lastName)
        .putString(SHARED_PREFS_SELF_USER_AVATAR, user.avatar?.fileName)
        .apply()
}

fun SharedPreferences.getSelfUser(): User? {
    val id = getString(SHARED_PREFS_SELF_USER_ID, "")?.toUUID() ?: return null
    val email = getString(SHARED_PREFS_SELF_USER_EMAIL, "") ?: return null
    val avatar = getString(SHARED_PREFS_SELF_USER_AVATAR, "")?.let { Avatar(it) }

    val firstName = getString(SHARED_PREFS_SELF_USER_FIRST_NAME, "") ?: return null
    val lastName = getString(SHARED_PREFS_SELF_USER_LAST_NAME, "") ?: return null
    return User(
        id, avatar, email, firstName, lastName, getSelectedWorkspace()
    )
}

fun SharedPreferences.getSelectedWorkspace(): Workspace? {
    val id = getString(SHARED_PREFS_SELECTED_WORKSPACE_ID, "")?.toUUID() ?: return null
    val title = getString(SHARED_PREFS_SELECTED_WORKSPACE_TITLE, "") ?: return null
    val organizationId =
        getString(SHARED_PREFS_SELECTED_WORKSPACE_ORGANIZATION_ID, "")?.toUUID() ?: return null
    return Workspace(id, title, organizationId)
}

fun SharedPreferences.saveSelectedWorkspace(workspace: Workspace) {
    edit()
        .putString(SHARED_PREFS_SELECTED_WORKSPACE_ID, workspace.id.toString())
        .putString(SHARED_PREFS_SELECTED_WORKSPACE_TITLE, workspace.title)
        .putString(
            SHARED_PREFS_SELECTED_WORKSPACE_ORGANIZATION_ID,
            workspace.organizationId.toString()
        )
        .apply()
}

fun SharedPreferences.clearAuthData() {
    edit().remove(SHARED_PREFS_AUTH_DATA).apply()
}

fun Context.getWorkspaceTitle(): String = getPrefs().getString(SELECTED_WORKSPACE_TITLE, "") ?: ""

fun Context.getSelectedWorkspaceUUID(): UUID? {
    return try {
        UUID.fromString(getPrefs().getString(SELECTED_WORKSPACE_ID, ""))
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun Context.getSelectedOrganizationUUID(): UUID? {
    return try {
        UUID.fromString(getPrefs().getString(SELECTED_ORGANIZATION_ID, ""))
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun Context.getUserEmail(): String = getPrefs().getString(USER_EMAIL, "") ?: ""
fun Context.getUserName(): String = getPrefs().getString(USER_NAME, "") ?: ""

fun Context.saveSelectedWorkspace(workspace: Workspace) {
    getPrefs().edit()
        .putString(SELECTED_WORKSPACE_TITLE, workspace.title)
        .putString(SELECTED_WORKSPACE_ID, workspace.id.toString())
        .apply()
}

fun Context.saveSelectedOrganization(org: Organization) {
    getPrefs().edit()
        .putString(SELECTED_ORGANIZATION_ID, org.id.toString())
        .apply()
}

fun Context.saveUsername(name: String) {
    getPrefs().edit()
        .putString(USER_NAME, name)
        .apply()
}

