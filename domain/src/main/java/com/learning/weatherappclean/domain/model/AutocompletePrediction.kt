package com.learning.weatherappclean.domain.model

data class AutocompletePrediction(val searchString:String, val predictions: List<String> = listOf()) {

}