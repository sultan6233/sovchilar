package com.sovchilar.made

import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat.isPaddingRelative
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sovchilar.made.presentation.activity.MainActivity
import com.sovchilar.made.presentation.fragments.view.AddFragment
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AddFragmentTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        launchFragmentInContainer<AddFragment>(
            themeResId = R.style.Theme_Sovchilar
        )
    }

    @Test
    fun checkTextViewPosition() {
        onView(withId(R.id.tvDescription))
            .check(matches(withParent(withId(R.id.clAdd)))) // Replace with your actual parent layout if necessary
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTedTelegramWithSymbol() {
        onView(withId(R.id.tedTelegram)).perform(typeText("@gmjmgmjm"))
        onView(withId(R.id.tedTelegram)).perform(typeText("@@@@"))
        onView(withId(R.id.tedTelegram)).check(matches(isDisplayed()))
            .check(matches(withText("@gmjmgmjm")))
    }

    @Test
    fun testTedTelegramWithInput() {
        onView(withId(R.id.tedTelegram)).perform(ViewActions.replaceText("@@@@@@@@@@@"))
        onView(withId(R.id.tedTelegram)).check(matches(isDisplayed()))
            .check(matches(withText("@")))
    }
}


