package com.learning.weatherappclean.di

import android.content.Context
import com.learning.weatherapp.network.RetrofitHelper
import com.learning.weatherappclean.data.remote_source.WeatherApi
import com.learning.weatherappclean.data.repository.LocalRepositoryImpl
import com.learning.weatherappclean.data.local_source.LocalStorage
import com.learning.weatherappclean.data.local_source.sharedprefs.SharedPrefsLocalStorage
import com.learning.weatherappclean.data.repository.RemoteRepositoryImpl
import com.learning.weatherappclean.domain.repository.LocalRepository
import com.learning.weatherappclean.domain.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    // val userRepository= UserRepositoryImpl(userStorage = SharedPrefsUserStorage(context=context))
    @Provides
    @Singleton
    fun provideLocalStorage(@ApplicationContext context: Context):LocalStorage{
        return SharedPrefsLocalStorage(context)
    }

    @Provides
    @Singleton
    fun provideLocalRepository (localStorage: LocalStorage):LocalRepository{
        return LocalRepositoryImpl(localStorage = localStorage)

    }

    @Provides
    @Singleton
    fun provideRemoteSource(): WeatherApi {
        return RetrofitHelper.getRetrofit()
    }

    @Provides
    @Singleton
    fun provideRemoteRepository (weatherAPI: WeatherApi): RemoteRepository {
        return RemoteRepositoryImpl(weatherAPI)

    }

}