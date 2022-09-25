package com.learning.weatherappclean.data.repository

import com.learning.weatherappclean.data.souce.local.LocalStorage
import com.learning.weatherappclean.data.model.WeatherRequests
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.LocalRepository
private const val SEPARATOR = "@"
class LocalRepositoryImpl(private val localStorage: LocalStorage) : LocalRepository {

    override fun saveWeatherCards(saveWeatherCardsList: List<WeatherCard>): Boolean {
        val weatherCardData = mapToStorage(saveWeatherCardsList)
        return localStorage.save(weatherCardData)
    }

    override fun loadWeatherCards():List<WeatherCard> {
        val weatherCardData = localStorage.get()
        return mapToDomain(weatherCardData)
    }

    private fun mapToStorage(saveWeatherCardsList: List<WeatherCard>):WeatherRequests{
        val list: MutableList<String> = mutableListOf()
        saveWeatherCardsList.forEach{list.add("${it.number}$SEPARATOR${it.location }, ${it.country}"   )}
        return WeatherRequests(content = list.toSet())
    }
    private fun mapToDomain(weatherRequests:WeatherRequests):List<WeatherCard>{
        val list: MutableList<WeatherCard> = mutableListOf()
            weatherRequests.content.forEach{ it ->
                val t = it.split(SEPARATOR)
                if (t.size>1) {
                    list.add(
                        WeatherCard(
                            location = t[1],
                            number = t[0].toInt(),
                            errorType = null
                        )
                    )
                }
                list.sortBy { it.number }
            }

        return list
    }
}