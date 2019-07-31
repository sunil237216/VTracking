package com.drushtilab

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDexApplication
import com.drushtilab.di.component.DaggerAppComponent
import com.drushtilab.di.module.ApiModule
import com.drushtilab.di.module.DbModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class AppController:MultiDexApplication(),HasActivityInjector {


    @Inject
    lateinit var mAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .apiModule(ApiModule())
            .dbModule(DbModule())
            .build()
            .inject(this)

    }
    override fun activityInjector(): DispatchingAndroidInjector<Activity> = mAndroidInjector

}