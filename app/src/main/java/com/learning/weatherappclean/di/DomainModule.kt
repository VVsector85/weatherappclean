package com.learning.weatherappclean.di

import com.learning.weatherappclean.domain.repository.LocalRepository
import com.learning.weatherappclean.domain.repository.RemoteRepository
import com.learning.weatherappclean.domain.usecase.GetAutocompletePredictionsUseCase
import com.learning.weatherappclean.domain.usecase.GetWeatherCardDataUseCase
import com.learning.weatherappclean.domain.usecase.SaveRequestListUseCase
import com.learning.weatherappclean.domain.usecase.LoadRequestListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideLoadWeatherCardsUseCase (localRepository:LocalRepository):LoadRequestListUseCase{
    return LoadRequestListUseCase(localRepository = localRepository)
    }

    @Provides
    fun provideSaveWeatherCardsUseCase (localRepository:LocalRepository): SaveRequestListUseCase {
    return SaveRequestListUseCase(localRepository = localRepository)
    }

    @Provides
    fun provideGetWeatherCardDataUseCase (remoteRepository: RemoteRepository):GetWeatherCardDataUseCase{
        return GetWeatherCardDataUseCase(remoteRepository=remoteRepository)
    }

    @Provides
    fun provideGetAutocompletePredictionsUseCase (remoteRepository: RemoteRepository): GetAutocompletePredictionsUseCase {
        return GetAutocompletePredictionsUseCase(remoteRepository=remoteRepository)
    }





}