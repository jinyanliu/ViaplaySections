package se.sugarest.jane.viaplaysections;

import android.content.Intent;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.viaplaysections.ui.detail.DetailFragment;
import se.sugarest.jane.viaplaysections.utilities.DrawableMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.IdlingRegistry.getInstance;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static se.sugarest.jane.viaplaysections.MainActivityTest.EspressoTestsMatchers.withDrawable;

/**
 * Created by jane on 17-11-16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;
    private Fragment mFragment;
    private MainActivity mMainActivity;

    @Before
    public void setUp() {

        // register IdlingResource
        mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        getInstance().register(mIdlingResource);

        // DetailFragment
        mFragment = new DetailFragment();
    }

    @Test
    public void testContent() {

        mMainActivity = activityTestRule.launchActivity(new Intent());

        mMainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_fragment_container, mFragment)
                .commit();

        if (mMainActivity.getmSectionNamesListForTesting() != null && !mMainActivity.getmSectionNamesListForTesting().isEmpty()) {

            // Menu image on the app bar
            onView(withId(R.id.navigation_menu)).check(matches(isDisplayed())).check(matches(notNullValue()))
                    .check(matches(withDrawable(R.drawable.ic_menu)));

            // Title on the app bar
            onView(withId(R.id.title_on_the_app_bar)).check(matches(isDisplayed())).check(matches(notNullValue()));

            // Viaplay logo on the app bar
            onView(withId(R.id.viaplay_logo)).check(matches(isDisplayed())).check(matches(notNullValue()))
                    .check(matches(withDrawable(R.drawable.viaplay_logo)));

            // Detail fragment container
            onView(withId(R.id.detail_fragment_container)).check(matches(isDisplayed())).check(matches(notNullValue()));


        } else {

            // Empty message image
            onView(withId(R.id.iv_empty_message)).check(matches(isDisplayed())).check(matches(notNullValue()))
                    .check(matches(withDrawable(R.drawable.pic_empty_message)));

        }
    }

    @Test
    public void testClickNavigationMenuImage_OpensDrawerNavigationContainer() {

        onView(withId(R.id.navigation_menu))
                .perform(click());

        onView(withId(R.id.navigation_drawer_container)).check(matches(isDisplayed()));
    }

    // Unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            getInstance().unregister(mIdlingResource);
        }
    }

    public static class EspressoTestsMatchers {

        public static Matcher<View> withDrawable(final int resourceId) {
            return new DrawableMatcher(resourceId);
        }

        public static Matcher<View> noDrawable() {
            return new DrawableMatcher(-1);
        }
    }
}
