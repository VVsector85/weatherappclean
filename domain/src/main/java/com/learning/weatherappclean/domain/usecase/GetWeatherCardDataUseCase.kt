package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository

class GetWeatherCardDataUseCase(private val remoteRepository: RemoteRepository) {

    suspend fun execute(location: WeatherCard): WeatherCard {
        val weatherCard = remoteRepository.getWeatherData(location)
/**This condition was in the task so I consider it as a part of business logic.
That is why it is in the domain module
No actual colours specified specified here, just enums*/
        if (!weatherCard.error) {
           /* when (weatherCard.temperature.toInt()) {
                in Int.MIN_VALUE..0 -> weatherCard.cardColor = ColorOption.BLUE
                in 1..20 -> weatherCard.cardColor = ColorOption.YELLOW
                in 21..Int.MAX_VALUE -> weatherCard.cardColor = ColorOption.RED
                else -> weatherCard.cardColor = ColorOption.DEFAULT
            }*/
        }
        return weatherCard
    }
}