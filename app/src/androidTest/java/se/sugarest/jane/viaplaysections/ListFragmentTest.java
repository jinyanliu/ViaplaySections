package se.sugarest.jane.viaplaysections;

import android.content.Intent;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.viaplaysections.ui.list.ListFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.IdlingRegistry.getInstance;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static se.sugarest.jane.viaplaysections.utilities.IgnoreCaseTextMatcher.withText;

/**
 * Created by jane on 17-12-1.
 */
@RunWith(AndroidJUnit4.class)
public class ListFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private Fragment mFragment;
    private MainActivity mMainActivity;
    private IdlingResource mIdlingResource;

    @Before
    public void setUp() {

        // register IdlingResource
        mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        getInstance().register(mIdlingResource);

        // ListFragment
        mFragment = new ListFragment();
    }

    @Test
    public void testClickNavigationMenuItem_DataChangeOnMainScreen() {

        mMainActivity = activityTestRule.launchActivity(new Intent());

        mMainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navigation_drawer_container, mFragment)
                .commit();

        if (mMainActivity.getmSectionNamesListForTesting() != null && !mMainActivity.getmSectionNamesListForTesting().isEmpty()) {

            String secondSectionName = mMainActivity.getmSectionNamesListForTesting().get(0);

            onView(withId(R.id.navigation_menu)).perform(click());

            onView(withId(R.id.navigation_drawer_container)).check(matches(isDisplayed()));

            onView(withId(R.id.navigation_drawer_recycler_view)).check(matches(isDisplayed()))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.title_on_the_app_bar)).check(matches(notNullValue()))
                    .check(matches(withText(secondSectionName)));
        }
    }

    // Unregister IdlingResource to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            getInstance().unregister(mIdlingResource);
        }
    }
}
