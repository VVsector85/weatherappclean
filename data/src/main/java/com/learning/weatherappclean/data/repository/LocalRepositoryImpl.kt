package com.learning.weatherappclean.data.repository


import com.learning.weatherappclean.data.souce.local.LocalStorage
import com.learning.weatherappclean.data.model.WeatherLocalData
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


    private fun mapToStorage(saveWeatherCardsList: List<WeatherCard>):WeatherLocalData{
        val list: MutableList<String> = mutableListOf()
        saveWeatherCardsList.forEach{list.add("${it.number }@${it.location }, ${it.country}"   )}
        return WeatherLocalData(content = list.toSet())
    }
    private fun mapToDomain(weatherLocalData:WeatherLocalData):List<WeatherCard>{
        val list: MutableList<WeatherCard> = mutableListOf()

            weatherLocalData.content.forEach{
                val t = it.split("@")
                list.add(WeatherCard(location = t[1], number =t[0].toInt()))}
                list.sortBy { it.number }
        return list
    }
}