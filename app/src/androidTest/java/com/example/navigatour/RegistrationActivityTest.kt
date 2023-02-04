package com.example.navigatour

import android.text.InputType
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class RegistrationActivityTest {
private lateinit var scenario: ActivityScenario<RegistrationActivity>
    @Before
    fun setUp() {
        scenario = launch(RegistrationActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showsProperRegistrationText(){
        onView(withId(R.id.registration_welcome)).check(matches(withText(R.string.registration_page_title)))
    }

    @Test
    fun signUpButtonIsUsable(){
        onView(withId(R.id.sign_up_button)).check(matches(isEnabled()))
        onView(withId(R.id.sign_up_button)).check(matches(isClickable()))
        onView(withId(R.id.sign_up_button)).check(matches(withText(R.string.registration_button_text)))
    }

    @Test
    fun inputTakesProperInputType(){
//        onView(withId(R.id.registration_username)).check(matches(withInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)))
//        onView(withId(R.id.registration_email)).check(matches(withInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)))
//        onView(withId(R.id.registration_password)).check(matches(withInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)))

    }
}