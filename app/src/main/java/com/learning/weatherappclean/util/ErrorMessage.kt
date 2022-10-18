package com.learning.weatherappclean.util

import com.learning.weatherappclean.R

class ErrorMessage(
    val errorType: ErrorTypeUi? = null,
    val showTime: Long = 0,
    private val errorCode: Int? = null,
    private val errorString: String = ""
) {
    fun getErrorMessage(): Pair<Int?, String> {
        when (errorType) {
            ErrorTypeUi.API_ERROR -> {
                return when (errorCode) {
                    101 -> Pair(R.string.code101, errorString)
                    102 -> Pair(R.string.code102, errorString)
                    104 -> Pair(R.string.code104, errorString)
                    601 -> Pair(R.string.code601, errorString)
                    602 -> Pair(R.string.code602, errorString)
                    615 -> Pair(R.string.code615, errorString)
                    else -> Pair(null, errorString)
                }
            }
            ErrorTypeUi.IO_ERROR ->
                return Pair(R.string.ioError, errorString)
            ErrorTypeUi.SAME_ITEM_ERROR ->
                return Pair(R.string.isOnTheList, errorString)
            else -> return Pair(null, errorString)
        }
    }
}
