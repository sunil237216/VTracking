package com.drushtilab.di.module


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.drushtilab.factory.ViewModelFactory


import com.drushtilab.ui.login.LoginViewModel
import com.drushtilab.ui.main.MainViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
//    @Binds
//    @IntoMap
//    @ViewModelKey(SearchViewModel::class)
//    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(RepoViewModel::class)
//    abstract fun bindRepoViewModel(repoViewModel: RepoViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}