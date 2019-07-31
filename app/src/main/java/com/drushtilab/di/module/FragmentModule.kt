package com.drushtilab.di.module

import com.drushtilab.ui.main.LandingFragment
import com.drushtilab.ui.main.MapFragment
import com.drushtilab.ui.main.ScanFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeScanFragment(): ScanFragment

    @ContributesAndroidInjector
    abstract fun contributeLandingFragment(): LandingFragment


    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapFragment
}