package com.drushtilab.di.component

import android.app.Application
import android.content.Context
import com.drushtilab.AppController
import com.drushtilab.di.module.*
import com.drushtilab.ui.login.LoginActivity
import com.drushtilab.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,ApiModule::class, DbModule::class,ViewModelModule::class , ActivityModule::class, FragmentModule::class])
interface AppComponent {

    fun inject(app: AppController)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun apiModule(apiModule: ApiModule): Builder

        @BindsInstance
        fun dbModule(dbmodule: DbModule): Builder

        fun build(): AppComponent
    }
}