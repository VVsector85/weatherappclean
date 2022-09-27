package com.learning.weatherappclean.di

import com.learning.weatherappclean.domain.repository.RequestsRepository
import com.learning.weatherappclean.domain.repository.RemoteRepository
import com.learning.weatherappclean.domain.repository.SettingsRepository
import com.learning.weatherappclean.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideLoadWeatherCardsUseCase (requestsRepository:RequestsRepository):LoadRequestListUseCase{
    return LoadRequestListUseCase(requestsRepository = requestsRepository)
    }

    @Provides
    fun provideSaveWeatherCardsUseCase (requestsRepository:RequestsRepository): SaveRequestListUseCase {
    return SaveRequestListUseCase(requestsRepository = requestsRepository)
    }

    @Provides
    fun provideGetWeatherCardDataUseCase (remoteRepository: RemoteRepository):GetWeatherCardDataUseCase{
        return GetWeatherCardDataUseCase(remoteRepository=remoteRepository)
    }

    @Provides
    fun provideGetAutocompletePredictionsUseCase (remoteRepository: RemoteRepository): GetAutocompletePredictionsUseCase {
        return GetAutocompletePredictionsUseCase(remoteRepository=remoteRepository)
    }

    @Provides
    fun provideLoadSettingsUseCase (settingsRepository: SettingsRepository):LoadSettingsUseCase{
        return LoadSettingsUseCase(settingsRepository = settingsRepository)
    }

    @Provides
    fun provideSaveSettingsUseCase (settingsRepository: SettingsRepository): SaveSettingsUseCase {
        return SaveSettingsUseCase(settingsRepository = settingsRepository)
    }



}