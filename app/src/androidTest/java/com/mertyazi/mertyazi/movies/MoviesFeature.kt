package com.mertyazi.mertyazi.movies

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.mertyazi.mertyazi.BaseUITest
import com.mertyazi.mertyazi.R
import com.mertyazi.mertyazi.di.idlingResource
import com.mertyazi.mertyazi.shared.Constants.FIRST_UPCOMING_MOVIE_RELEASE_DATE
import com.mertyazi.mertyazi.shared.Constants.FIRST_UPCOMING_MOVIE_DESCRIPTION
import com.mertyazi.mertyazi.shared.Constants.FIRST_UPCOMING_MOVIE_TITLE
import com.mertyazi.mertyazi.shared.presentation.MainActivity
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test

class MoviesFeature: BaseUITest() {

    val mActivityRule = ActivityTestRule(MainActivity::class.java)
        @Rule get

    @Test
    fun displaysListOfUpcomingMovies() {
        /*
        * In order to this test to pass, tester should run the request with postman
        * to get the first child's title, description and release date
        * and replace the ones in here.
        * */
        Thread.sleep(500)
        assertRecyclerViewItemCount(R.id.rv_upcoming, 20)

        onView(
            allOf(
                withId(R.id.tv_movie_title),
                isDescendantOfA(nthChildOf(withId(R.id.rv_upcoming), 0))
            )
        )
            .check(matches(withText(FIRST_UPCOMING_MOVIE_TITLE)))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.tv_movie_description),
                isDescendantOfA(nthChildOf(withId(R.id.rv_upcoming), 0))
            )
        )
            .check(matches(withText(FIRST_UPCOMING_MOVIE_DESCRIPTION)))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.tv_movie_date),
                isDescendantOfA(nthChildOf(withId(R.id.rv_upcoming), 0))
            )
        )
            .check(matches(withText(FIRST_UPCOMING_MOVIE_RELEASE_DATE)))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.iv_movie),
                isDescendantOfA(nthChildOf(withId(R.id.rv_upcoming), 0))
            )
        )
            .check(matches(isDisplayed()))
    }

    @Test
    fun displaysListOfNowPlayingMovies() {
        assertDisplayed(R.id.sv_now_playing)

        onView(
            allOf(
                withId(R.id.tv_now_playing_title),
                isDisplayed()
            )
        )

        onView(
            allOf(
                withId(R.id.tv_now_playing_description),
                isDisplayed()
            )
        )

        onView(
            allOf(
                withId(R.id.iv_now_playing),
                isDisplayed()
            )
        )

    }

    @Test
    fun displaysLoaderWhileFetchingMovies() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        assertDisplayed(R.id.loader)
    }

    @Test
    fun hideLoader() {
        Thread.sleep(500)
        assertNotDisplayed(R.id.loader)
    }

    @Test
    fun navigateToMovieDetailsScreen() {
        Thread.sleep(500)
        onView(
            allOf(
                withId(R.id.iv_movie),
                isDescendantOfA(nthChildOf(withId(R.id.rv_upcoming), 0))
            )
        )
            .perform(click())

        assertDisplayed(R.id.movie_details_root)
    }

}