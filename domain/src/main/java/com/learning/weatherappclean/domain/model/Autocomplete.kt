package com.learning.weatherappclean.domain.model

data class Autocomplete(val searchString:String="",
                        val predictions: List<Predictions> = listOf(),
                        val error: Boolean = false,
                        val errorType: Any? = null,
                        val errorMsg:String = ""
                                  ) {

    data class Predictions(
        val location:String,
        val country:String,
        val region:String,
        val lat:String,
        val lon:String
    )
}