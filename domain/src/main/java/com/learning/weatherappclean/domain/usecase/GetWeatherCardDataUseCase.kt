package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.ResourceDomain
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository

class GetWeatherCardDataUseCase(private val remoteRepository: RemoteRepository) {

    suspend operator fun invoke(request: Request): ResourceDomain<WeatherCard> {
        val resourceDomainCard = remoteRepository.getWeatherData(request)

        if (resourceDomainCard is ResourceDomain.Success && resourceDomainCard.data != null) {
            /**This condition was in the task so I consider it as a part of business logic.
             That is why it is in the domain module
             No actual colours specified here, just enums*/

            val temperature = resourceDomainCard.data.temperature.toInt()
            with(resourceDomainCard.data) {
                if (this.units == "m") when (temperature) {
                    in BLUE_RANGE_CELSIUS -> this.cardColorOption = CardColorOption.BLUE
                    in YELLOW_RANGE_CELSIUS -> this.cardColorOption = CardColorOption.YELLOW
                    else -> this.cardColorOption = CardColorOption.RED
                }
                else when (temperature) {
                    in BLUE_RANGE_FAHRENHEIT -> this.cardColorOption = CardColorOption.BLUE
                    in YELLOW_RANGE_FAHRENHEIT -> this.cardColorOption = CardColorOption.YELLOW
                    else -> this.cardColorOption = CardColorOption.RED
                }
            }
        }
        resourceDomainCard.data?.showDetails = request.showDetails
        return resourceDomainCard
    }

    companion object {
        val BLUE_RANGE_CELSIUS = Int.MIN_VALUE..0
        val YELLOW_RANGE_CELSIUS = 1..20
        val BLUE_RANGE_FAHRENHEIT = Int.MIN_VALUE..33
        val YELLOW_RANGE_FAHRENHEIT = 34..68
    }
}
