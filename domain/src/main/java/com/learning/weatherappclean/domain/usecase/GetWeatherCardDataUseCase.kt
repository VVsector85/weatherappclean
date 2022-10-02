package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository

class GetWeatherCardDataUseCase(private val remoteRepository: RemoteRepository) {

    suspend fun execute(request: Request): WeatherCard {
        var weatherCard = remoteRepository.getWeatherData(request)

        if (!weatherCard.error) {
            /**I had to add this code to to fix some inconsistencies in Weatherstack API behaviour:
             in case of specifying Region with API's own autocomplete sometimes it returns
             wrong location, so if the location does not match the query the app sends another
             request with latitude and longitude. Relying on coordinates alone also does not help
             because API often returns location name which does not match autocomplete suggestion.*/
           if (request.location!=weatherCard.location&&request.location!=""){
                weatherCard = remoteRepository.getWeatherData(Request(
                    query = "${request.lat}, ${request.lon}"))
            }
            /**This condition was in the task so I consider it as a part of business logic.
            That is why it is in the domain module
            No actual colours specified specified here, just enums*/
            if (!weatherCard.error) {
                val temperature = weatherCard.temperature.toInt()
                if (weatherCard.units == "m") when (temperature) {
                    in Int.MIN_VALUE..0 -> weatherCard.cardColorOption = CardColorOption.BLUE
                    in 1..20 -> weatherCard.cardColorOption = CardColorOption.YELLOW
                    in 21..Int.MAX_VALUE -> weatherCard.cardColorOption = CardColorOption.RED
                    else -> weatherCard.cardColorOption = CardColorOption.DEFAULT
                }
                else when (temperature) {
                    in Int.MIN_VALUE..32 -> weatherCard.cardColorOption = CardColorOption.BLUE
                    in 34..68 -> weatherCard.cardColorOption = CardColorOption.YELLOW
                    in 67..Int.MAX_VALUE -> weatherCard.cardColorOption = CardColorOption.RED
                    else -> weatherCard.cardColorOption = CardColorOption.DEFAULT
                }
            }
        }
        weatherCard.showDetails = request.showDetails
        return weatherCard
    }
}