package com.telefonica.mocks.common

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val throwable: Throwable? = null) : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[message=${throwable?.message}]"
        }
    }
}

val <T>ApiResult<T>.data: T? get() = (this as? ApiResult.Success)?.data

fun ApiResult.Error.getMessage(): String = this.throwable?.message ?: "Something Wrong, try later"
