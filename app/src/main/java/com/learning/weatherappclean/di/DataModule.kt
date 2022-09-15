package com.learning.weatherappclean.di

import android.content.Context
import com.learning.weatherappclean.data.repository.LocalRepositoryImpl
import com.learning.weatherappclean.data.storage.LocalStorage
import com.learning.weatherappclean.data.storage.sharedprefs.SharedPrefsLocalStorage
import com.learning.weatherappclean.domain.repository.LocalRepository
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

}