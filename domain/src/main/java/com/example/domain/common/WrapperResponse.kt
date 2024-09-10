package com.example.domain.common

class WrapperResponse<T>(val exception: Throwable? = null, val response: T? = null) {
    val isSuccess = response != null
}

suspend fun <T> executeWrapperAPICall(action: suspend () -> T): WrapperResponse<T> {
    try {
        val response  = action.invoke()
        return WrapperResponse(response = response)
    } catch (ex: Exception) {
        return WrapperResponse(exception = ex)
    }
}