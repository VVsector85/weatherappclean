package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.ResourceDomain
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository


class GetWeatherCardDataUseCase(private val remoteRepository: RemoteRepository) {

    suspend operator fun invoke(request: Request): ResourceDomain<WeatherCard> {
        var resourceDomainCard = remoteRepository.getWeatherData(request)

        if (resourceDomainCard is ResourceDomain.Success) {

            /**I had to add this code to to fix some inconsistencies in Weatherstack API behaviour:
            in case of specifying Region with API's own autocomplete sometimes it returns
            wrong location, so if the location does not match the query the app sends another
            request with latitude and longitude. Relying on coordinates alone also does not help
            because API often returns location name which does not match autocomplete suggestion.*/

            if (request.location != resourceDomainCard.data!!.location && request.location != "") {
                resourceDomainCard = remoteRepository.getWeatherData(
                    Request(
                        query = "${request.lat}, ${request.lon}"
                    )
                )
            }

            /**This condition was in the task so I consider it as a part of business logic.
            That is why it is in the domain module
            No actual colours specified here, just enums*/

            if (resourceDomainCard is ResourceDomain.Success && resourceDomainCard.data != null) {
                val temperature = resourceDomainCard.data!!.temperature.toInt()
                with(resourceDomainCard.data!!)
                {
                    if (this.units == "m") when (temperature) {
                        in Int.MIN_VALUE..0 -> this.cardColorOption = CardColorOption.BLUE
                        in 1..20 -> this.cardColorOption = CardColorOption.YELLOW
                        in 21..Int.MAX_VALUE -> this.cardColorOption = CardColorOption.RED
                        else -> this.cardColorOption = CardColorOption.DEFAULT
                    }
                    else when (temperature) {
                        in Int.MIN_VALUE..32 -> this.cardColorOption = CardColorOption.BLUE
                        in 34..68 -> this.cardColorOption = CardColorOption.YELLOW
                        in 67..Int.MAX_VALUE -> this.cardColorOption = CardColorOption.RED
                        else -> this.cardColorOption = CardColorOption.DEFAULT
                    }
                }
            }
        }
        resourceDomainCard.data?.showDetails = request.showDetails
        return resourceDomainCard
    }
}