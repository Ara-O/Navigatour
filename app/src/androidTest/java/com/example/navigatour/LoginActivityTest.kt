package com.example.navigatour

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginActivityTest {
    private lateinit var scenario: ActivityScenario<LoginActivity>
    @Before
    fun setUp() {
        scenario = launch(LoginActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showsProperLoginText(){
        Espresso.onView(ViewMatchers.withId(R.id.login_welcome))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.login_page_title)))
    }

    @Test
    fun loginButtonIsUsable(){
        Espresso.onView(ViewMatchers.withId(R.id.login_button))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.login_button))
            .check(ViewAssertions.matches(ViewMatchers.isClickable()))
        Espresso.onView(ViewMatchers.withId(R.id.login_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.login_button_text)))
    }
}