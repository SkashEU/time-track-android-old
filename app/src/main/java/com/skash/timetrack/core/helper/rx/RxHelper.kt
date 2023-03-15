package com.skash.timetrack.core.helper.rx

import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import io.reactivex.rxjava3.core.Observable

fun <T : Any> Observable<T>.toState(errorType: (Throwable) -> ErrorType): Observable<State<T>> {
    return Observable.concat(
        Observable.just(State.Loading()),
        this.map<State<T>> {
            State.Success(it)
        }.onErrorReturn {
            State.Error(ErrorType.fromThrowable(it) ?: errorType(it))
        }
    )
}