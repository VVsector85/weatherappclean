package com.learning.weatherappclean.domain.model

sealed class ResourceDomain<T> {
    open val data: T? = null
    data class Success<T>(override val data: T) : ResourceDomain<T>()
    data class Error<T>(
        val type: Any,
        val message: String,
        val code: Int?
    ) : ResourceDomain<T>()
}
