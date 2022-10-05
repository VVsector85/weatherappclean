package com.learning.weatherappclean.data.di

import com.learning.weatherappclean.data.souce.remote.WeatherApi
import com.learning.weatherappclean.data.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun provideRetrofit(): WeatherApi =
        Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            /*.addConverterFactory(MoshiConverterFactory.create())*/
            /*.addConverterFactory(GsonConverterFactory.create())*/
            .build()
            .create(WeatherApi::class.java)

    private fun getOkHttpClient() =
        OkHttpClient.Builder()
            .connectTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
}
