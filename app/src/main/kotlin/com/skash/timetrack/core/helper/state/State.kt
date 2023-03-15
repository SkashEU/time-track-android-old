package com.skash.timetrack.core.helper.state

import android.content.Context
import com.skash.timetrack.core.helper.context.showErrorAlert
import com.skash.timetrack.core.helper.state.loading.LoadingDialog

sealed class State<T> {
    class Success<T>(val value: T) : State<T>()
    class Loading<T> : State<T>()
    class Error<T>(val errorType: ErrorType) : State<T>()

    fun valueOrNull(): T? {
        if (this is Success) {
            return this.value
        }
        return null
    }
}

inline fun <T> State<T>.handle(
    context: Context,
    loadingDialog: LoadingDialog?,
    onSuccess: (T) -> Unit,
    // Return false of you did not handle the error and wishes to let the function handle the error
    handleError: (ErrorType) -> Boolean = { false },
) {
    when (this) {
        is State.Error -> {
            loadingDialog?.dismiss()

            if (handleError(errorType)) {
                return
            }
            context.showErrorAlert(this.errorType)
        }

        is State.Loading -> loadingDialog?.show()
        is State.Success -> {
            loadingDialog?.dismiss()
            onSuccess(this.value)
        }
    }
}