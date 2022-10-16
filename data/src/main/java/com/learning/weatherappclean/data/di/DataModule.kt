package com.learning.weatherappclean.data.di

import com.learning.weatherappclean.data.souce.remote.WeatherApi
import com.learning.weatherappclean.data.util.Constants
import com.learning.weatherappclean.data.util.Constants.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
            .build()
            .create(WeatherApi::class.java)

    private fun getOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor { chain -> return@addInterceptor addApiKeyToRequests(chain) }
            .connectTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    private fun addApiKeyToRequests(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter(name = "access_key", value = API_KEY).build()
        request.url(newUrl)
        return chain.proceed(request.build())
    }
}
