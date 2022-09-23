package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository

class GetWeatherCardDataUseCase(private val remoteRepository: RemoteRepository) {

    suspend fun execute(request: Request): WeatherCard {
        val weatherCard = remoteRepository.getWeatherData(request)
/**This condition was in the task so I consider it as a part of business logic.
That is why it is in the domain module
No actual colours specified specified here, just enums*/
        if (!weatherCard.error) {
            when (weatherCard.temperature.toInt()) {
                in Int.MIN_VALUE..0 -> weatherCard.cardColorOption = CardColorOption.BLUE
                in 1..20 -> weatherCard.cardColorOption = CardColorOption.YELLOW
                in 21..Int.MAX_VALUE -> weatherCard.cardColorOption = CardColorOption.RED
                else -> weatherCard.cardColorOption = CardColorOption.DEFAULT
            }
        }
        return weatherCard
    }
}