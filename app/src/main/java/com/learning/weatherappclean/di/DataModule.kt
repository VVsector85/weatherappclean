package com.learning.weatherappclean.di

import android.content.Context
import com.learning.weatherappclean.data.repository.RemoteRepositoryImpl
import com.learning.weatherappclean.data.repository.RequestsRepositoryImpl
import com.learning.weatherappclean.data.repository.SettingsRepositoryImpl
import com.learning.weatherappclean.data.source.local.RequestsStorage
import com.learning.weatherappclean.data.source.local.SettingsStorage
import com.learning.weatherappclean.data.source.local.sharedprefs.SharedPrefsRequestsStorage
import com.learning.weatherappclean.data.source.local.sharedprefs.SharedPrefsSettingsStorage
import com.learning.weatherappclean.data.source.remote.WeatherApi
import com.learning.weatherappclean.domain.repository.RemoteRepository
import com.learning.weatherappclean.domain.repository.RequestsRepository
import com.learning.weatherappclean.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRequestsStorage(@ApplicationContext context: Context): RequestsStorage {
        return SharedPrefsRequestsStorage(context)
    }

    @Provides
    @Singleton
    fun provideRequestsRepository(requestsStorage: RequestsStorage): RequestsRepository {
        return RequestsRepositoryImpl(requestsStorage = requestsStorage)
    }

    @Provides
    @Singleton
    fun provideSettingsStorage(@ApplicationContext context: Context): SettingsStorage {
        return SharedPrefsSettingsStorage(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsStorage: SettingsStorage): SettingsRepository {
        return SettingsRepositoryImpl(settingsStorage = settingsStorage)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(weatherAPI: WeatherApi): RemoteRepository {
        return RemoteRepositoryImpl(weatherAPI)
    }
}
