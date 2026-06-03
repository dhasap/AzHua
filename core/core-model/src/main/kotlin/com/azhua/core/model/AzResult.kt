package com.azhua.core.model

/**
 * Generic result wrapper for async operations.
 * Used across all layers for consistent error handling.
 */
sealed class AzResult<out T> {
    data class Success<T>(val data: T) : AzResult<T>()
    data class Error(val message: String, val cause: Throwable? = null) : AzResult<Nothing>()
    data object Loading : AzResult<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? = (this as? Success)?.data

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw cause ?: IllegalStateException(message)
        is Loading -> throw IllegalStateException("Result is still loading")
    }

    fun <R> map(transform: (T) -> R): AzResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(message, cause)
        is Loading -> Loading
    }

    suspend fun <R> suspendMap(transform: suspend (T) -> R): AzResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(message, cause)
        is Loading -> Loading
    }
}
