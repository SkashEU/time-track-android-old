package com.skash.timetrack.core.helper.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.skash.timetrack.core.model.Organization
import com.skash.timetrack.core.model.Workspace
import java.util.UUID

const val SELECTED_ORGANIZATION_ID = "key_selected_organization_id"
const val SELECTED_WORKSPACE_ID = "key_selected_workspace_id"
const val SELECTED_WORKSPACE_TITLE = "key_selected_workspace_title"
const val USER_EMAIL = "key_user_email"
const val USER_NAME = "key_user_name"

fun Context.getPrefs(): SharedPreferences {
    return getSharedPreferences(packageName, Context.MODE_PRIVATE)
}

fun Context.getSelectedWorkspace(): Workspace? {
    val id: UUID = try {
        UUID.fromString(getPrefs().getString(SELECTED_WORKSPACE_ID, ""))
    } catch (e: IllegalArgumentException) {
        null
    } ?: return null

    val title = getPrefs().getString(SELECTED_WORKSPACE_TITLE, "").run {
        if (this.isNullOrEmpty()) {
            return null
        }

        return@run this
    }

    return Workspace(id, title)
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

