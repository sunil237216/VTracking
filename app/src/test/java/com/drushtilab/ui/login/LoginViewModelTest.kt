package com.drushtilab.ui.login

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    lateinit var SUT: LoginViewModel()


    @Before
    fun setup() {
        SUT = LoginViewModel()
    }


}