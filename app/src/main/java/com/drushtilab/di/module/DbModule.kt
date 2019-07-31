package com.drushtilab.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DbModule {


    @Provides
    @Singleton
     fun providePreferences(
        application: Application
    ): SharedPreferences {
        return application.getSharedPreferences(
            "store", Context.MODE_PRIVATE
        )
    }
}