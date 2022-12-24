package com.mertyazi.mertyazi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.mertyazi.mertyazi.other.Constants.THE_WOMAN_KING_DATE
import com.mertyazi.mertyazi.other.Constants.THE_WOMAN_KING_DESCRIPTION
import com.mertyazi.mertyazi.other.Constants.THE_WOMAN_KING_TITLE
import com.mertyazi.mertyazi.ui.MainActivity
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test

class MoviesFeature: BaseUITest() {

    val mActivityRule = ActivityTestRule(MainActivity::class.java)
        @Rule get

    @Test
    fun displaysListOfUpcomingMovies() {
        assertRecyclerViewItemCount(R.id.rv_upcoming, 20)

        onView(
            allOf(
                withId(R.id.tv_movie_title),
                isDescendantOfA(nthChildOf(withId(R.id.rv_upcoming), 0))
            )
        )
            .check(matches(withText(THE_WOMAN_KING_TITLE)))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.tv_movie_description),
                isDescendantOfA(nthChildOf(withId(R.id.rv_upcoming), 0))
            )
        )
            .check(matches(withText(THE_WOMAN_KING_DESCRIPTION)))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.tv_movie_date),
                isDescendantOfA(nthChildOf(withId(R.id.rv_upcoming), 0))
            )
        )
            .check(matches(withText(THE_WOMAN_KING_DATE)))
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
    fun navigateToMovieDetailsScreen() {
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