package com.learning.weatherappclean.data.repository


import android.util.Log
import com.learning.weatherappclean.data.local_source.LocalStorage
import com.learning.weatherappclean.data.model.WeatherCardData
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.LocalRepository




class LocalRepositoryImpl(private val localStorage: LocalStorage) : LocalRepository {



    override fun saveWeatherCards(saveWeatherCardsList: List<WeatherCard>): Boolean {

        val weatherCardData = mapToStorage(saveWeatherCardsList)
        return localStorage.save(weatherCardData)
    }

    override fun loadWeatherCards():List<WeatherCard> {
        val weatherCardData = localStorage.get()
        return mapToDomain(weatherCardData)
    }


    private fun mapToStorage(saveWeatherCardsList: List<WeatherCard>):WeatherCardData{
        val list: MutableList<String> = mutableListOf()
        saveWeatherCardsList.forEach{list.add("${it.number }@${it.location }, ${it.country}"   )}
        return WeatherCardData(content = list.toSet())
    }
    private fun mapToDomain(weatherCardData:WeatherCardData):List<WeatherCard>{
        val list: MutableList<WeatherCard> = mutableListOf()

            weatherCardData.content.forEach{
                val t = it.split("@")
                list.add(WeatherCard(location = t[1], number =t[0].toInt()))}
                list.sortBy { it.number }
        return list
    }
}