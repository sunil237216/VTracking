package com.drushtilab.di.module

import com.drushtilab.ui.login.LoginActivity
import com.drushtilab.ui.main.MainActivity
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule{

    @Provides()
    fun contributeLoginActivity(loginActivity: LoginActivity):
            LoginActivity =loginActivity



    @Provides()
    fun contributeMainActivity(mainActivity: MainActivity):
            MainActivity =mainActivity

}