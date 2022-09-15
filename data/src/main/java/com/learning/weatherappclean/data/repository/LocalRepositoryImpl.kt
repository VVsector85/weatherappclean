package com.learning.weatherappclean.data.repository


import com.learning.weatherappclean.data.storage.LocalStorage
import com.learning.weatherappclean.data.storage.model.WeatherCardData
import com.learning.weatherappclean.domain.model.AddWeatherCardParams
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
        var list: MutableList<String> = mutableListOf()
        saveWeatherCardsList.forEach{list.add(it.location)}

        return WeatherCardData(content = list.toMutableSet())
    }
    private fun mapToDomain(weatherCardData:WeatherCardData):List<WeatherCard>{
        var list: MutableList<WeatherCard> = mutableListOf()
            weatherCardData.content.forEach { list.add(WeatherCard(location = it))}
        return list
    }
}