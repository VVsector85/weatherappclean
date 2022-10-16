package com.learning.weatherappclean.data.model.dto.weatherdata

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)
