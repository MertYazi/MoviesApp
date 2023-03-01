package com.mertyazi.mertyazi.favorites

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.mertyazi.mertyazi.BaseUITest
import com.mertyazi.mertyazi.R
import com.mertyazi.mertyazi.di.idlingResource
import com.mertyazi.mertyazi.shared.presentation.MainActivity
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test

class FavoritesFeature : BaseUITest() {

    val mActivityRule = ActivityTestRule(MainActivity::class.java)
        @Rule get

    /*
    * Make sure that you've added some favorite movies
    * */

    @Test
    fun displaysListOfFavorites() {
        navigateToFavoritesScreen()
        Thread.sleep(500)
        onView(
            allOf(
                withId(R.id.iv_favorite),
                isDescendantOfA(nthChildOf(withId(R.id.rv_favorite_movies), 0))
            )
        )
            .check(matches(isDisplayed()))
    }

    @Test
    fun displaysRandomFavoriteMovieAfterRolling() {
        navigateToFavoritesScreen()
        Thread.sleep(500)

        onView(
            withId(R.id.iv_favorite_movies_random)
        )
            .perform(click())

        onView(
            withId(R.id.iv_favorite_movies)
        )
            .check(matches(isDisplayed()))
    }

    @Test
    fun displaysLoaderWhileFetchingTheFavorites() {
        /*
        * This can be fail in order to get low amount of data happens too fast
        * */
        IdlingRegistry.getInstance().unregister(idlingResource)
        navigateToFavoritesScreen()
        assertDisplayed(R.id.loader)
    }

    @Test
    fun hideLoader() {
        navigateToFavoritesScreen()
        Thread.sleep(500)
        assertNotDisplayed(R.id.loader)
    }

    private fun navigateToFavoritesScreen() {
        Thread.sleep(500)
        onView(
            withId(R.id.favoritesFragment)
        )
            .perform(click())
    }
}