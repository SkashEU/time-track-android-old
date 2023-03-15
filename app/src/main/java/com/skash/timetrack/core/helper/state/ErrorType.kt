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

}

