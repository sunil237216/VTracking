package com.drushtilab

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.drushtilab.ui.login.LoginActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {


    @Rule
    @JvmField
    val rule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.drushtilab", appContext.packageName)
    }

    @Test
    fun user_can_enter_username(){

        onView(withId(R.id.username)).perform(typeText("sunil123"))
        onView(withId(R.id.password)).perform(typeText("8652865259"))
        onView(withId(R.id.btnlogin)).perform(click())

    }

    @Test
    fun user_can_enter_wrong(){

        onView(withId(R.id.username)).perform(typeText("sunil123"))
        onView(withId(R.id.password)).perform(typeText("86285259"))
        onView(withId(R.id.btnlogin)).perform(click())
    }

    @Test
    fun user_can_enter_empty(){

        onView(withId(R.id.username)).perform(typeText(""))
        onView(withId(R.id.password)).perform(typeText(""))
        onView(withId(R.id.btnlogin)).perform(click())
    }




}
