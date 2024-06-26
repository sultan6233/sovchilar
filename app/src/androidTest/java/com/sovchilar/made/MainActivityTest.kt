package com.sovchilar.made

import android.widget.ImageView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sovchilar.made.presentation.activity.MainActivity
import com.sovchilar.made.presentation.fragments.view.ChangeLanguageFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun testNavigationToInGameScreen() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<ChangeLanguageFragment>()

        titleScenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController’s state
        onView(withId(R.id.btnNext)).perform(click())
    }
    @Test
    fun testImageView() {
        onView(withId(R.id.blurBg))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.isAssignableFrom(ImageView::class.java)))
    }

    @Test
    fun testBottomNav() {
        onView(withId(R.id.btnNext)).perform(click())
        onView(withId(R.id.accountContainerFragment)).perform(click())
        onView(withId(R.id.tedName)).perform(typeText("somenicj"))
        onView(withId(R.id.tedPassword)).perform(typeText("www123"))
        onView(withId(R.id.btnLoginOrRegister)).perform(click())
        var count = 0
        while (count < 100) {
            count++
            onView(withId(R.id.accountContainerFragment)).perform(click())
            onView(withId(R.id.btnAddAdvertisement)).perform(click())
            onView(withId(R.id.accountContainerFragment)).perform(click())
        //    onView(withId(R.id.accountFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(withId(R.id.advertisementFragment)).perform(click())
        }
    }
}


