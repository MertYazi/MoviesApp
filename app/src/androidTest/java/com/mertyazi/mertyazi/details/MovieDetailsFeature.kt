package com.mertyazi.mertyazi.details

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.mertyazi.mertyazi.BaseUITest
import com.mertyazi.mertyazi.R
import com.mertyazi.mertyazi.di.idlingResource
import com.mertyazi.mertyazi.shared.Constants.FIRST_UPCOMING_MOVIE_DESCRIPTION
import com.mertyazi.mertyazi.shared.Constants.FIRST_UPCOMING_MOVIE_RELEASE_DATE
import com.mertyazi.mertyazi.shared.Constants.FIRST_UPCOMING_MOVIE_TITLE
import com.mertyazi.mertyazi.shared.presentation.MainActivity
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.internal.matcher.DrawableMatcher.Companion.withDrawable
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

class MovieDetailsFeature : BaseUITest() {

    val mActivityRule = ActivityTestRule(MainActivity::class.java)
        @Rule get

    @Test
    fun displaysMovieTitleDescriptionAndReleaseDate() {
        navigateToMovieDetails(1)

        Thread.sleep(500)

        assertDisplayed(FIRST_UPCOMING_MOVIE_TITLE)

        assertDisplayed(FIRST_UPCOMING_MOVIE_DESCRIPTION)

        assertDisplayed(FIRST_UPCOMING_MOVIE_RELEASE_DATE)
    }

    @Test
    fun displaysNonFavoriteMovieIconChangeToFavorite() {
        navigateToMovieDetails(1)

        Thread.sleep(500)

        onView(
            withId(R.id.iv_favorite_movie)
        )
            .perform(click())

        Thread.sleep(500)

        onView(
            withId(R.id.iv_favorite_movie)
        )
            .check(matches(withDrawable(R.drawable.ic_favorite_unselected)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displaysLoaderWhileFetchingTheMovieDetails() {
        IdlingRegistry.getInstance().unregister(idlingResource)

        navigateToMovieDetails(1)

        assertDisplayed(R.id.loader)
    }

    @Test
    fun hidesLoader() {
        navigateToMovieDetails(1)
        Thread.sleep(500)
        assertNotDisplayed(R.id.loader)
    }

    private fun navigateToMovieDetails(row: Int) {
        Thread.sleep(500)
        onView(
            allOf(
                withId(R.id.iv_movie),
                isDescendantOfA(
                    nthChildOf(
                        withId(R.id.rv_upcoming),
                        row
                    )
                )
            )
        )
            .perform(click())
    }
}