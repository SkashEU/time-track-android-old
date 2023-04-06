package com.skash.timetrack.core.helper.rx

import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import io.reactivex.rxjava3.core.Observable
import retrofit2.HttpException

fun <T : Any> Observable<T>.toState(errorType: (Throwable) -> ErrorType): Observable<State<T>> {
    return Observable.concat(
        Observable.just(State.Loading()),
        this.map<State<T>> {
            State.Success(it)
        }.onErrorReturn {
            val httpError = it as? HttpException ?: return@onErrorReturn State.Error(errorType(it))
            State.Error(ErrorType.fromThrowable(httpError) ?: errorType(it))
        }
    )
}