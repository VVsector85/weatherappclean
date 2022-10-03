package com.learning.weatherappclean.domain.model

data class Autocomplete(val searchString:String="",
                        val predictions: List<Prediction> = listOf(),
                        val error: Boolean = false,
                        val errorType: Any? = null,
                        val errorCode: Int? = null,
                        val errorMsg:String = ""
                                  ) {

    data class Prediction(
        val location:String,
        val country:String,
        val region:String,
        val lat:String,
        val lon:String
    )
}