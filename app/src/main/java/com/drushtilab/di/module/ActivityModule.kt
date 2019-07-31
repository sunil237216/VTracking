package com.drushtilab.di.module
import com.drushtilab.ui.login.LoginActivity
import com.drushtilab.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

//    @ContributesAndroidInjector(modules = [FragmentModule::class])
//    abstract fun contributeMainActivity(): MainActivity


    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity


    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity():MainActivity


}