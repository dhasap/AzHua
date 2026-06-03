package com.azhua.core.common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import com.azhua.core.model.AzResult

/**
 * Converts a Flow into AzResult Flow.
 */
fun <T> Flow<T>.asResult(): Flow<AzResult<T>> = this
    .map<T, AzResult<T>> { AzResult.Success(it) }
    .onStart { emit(AzResult.Loading) }
    .catch { emit(AzResult.Error(it.message ?: "Unknown error", it)) }
