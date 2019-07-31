package com.drushtilab

import android.content.SharedPreferences
import com.drushtilab.ui.login.LoginViewModel
import androidx.lifecycle.Observer
import com.drushtilab.repository.Repository
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Rule

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.drushtilab.network.ApiInterface
import com.drushtilab.network.model.LoginResponse
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito


class loginTest{

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userDao = Mockito.mock(SharedPreferences::class.java)
    private val githubService = Mockito.mock(ApiInterface::class.java)
    private val repo = Repository( githubService,userDao)

    private val loginRepository = Mockito.mock(Repository::class.java)

   // private val repoRepository = Mockito.mock(RepoRepository::class.java)
    private val userViewModel = LoginViewModel(loginRepository)




    @Test
    fun login()
    {
     val observer = mock<Observer<Int>>()
//        repo.login("test","test")
//
//        Mockito.verify(githubService, Mockito.never()).login("foo","test")

       // userViewModel.getLoginLiveData().observeForever(mock())

        userViewModel.onError.postValue(200)
        userViewModel.onError.observeForever(observer)

    }

}