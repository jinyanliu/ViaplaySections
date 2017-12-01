package se.sugarest.jane.viaplaysections.mainActivityTests;

import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.ui.MainActivity;
import se.sugarest.jane.viaplaysections.util.DrawableMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.IdlingRegistry.getInstance;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static se.sugarest.jane.viaplaysections.mainActivityTests.HasContentMainActivityTest.EspressoTestsMatchers.withDrawable;

/**
 * Created by jane on 17-11-16.
 */
@RunWith(AndroidJUnit4.class)
public class HasContentMainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        getInstance().register(mIdlingResource);
    }

    @Test
    public void mainScreenHasContent_menuImageOnTheAppBar() {
        onView(withId(R.id.navigation_menu)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withDrawable(R.drawable.ic_menu)));
    }

    @Test
    public void mainScreenHasContent_titleOnTheAppBar() {
        onView(withId(R.id.title_on_the_app_bar)).check(matches(isDisplayed())).check(matches(notNullValue()));
    }

    @Test
    public void mainScreenHasContent_viaplayLogoOnTheAppBar() {
        onView(withId(R.id.viaplay_logo)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withDrawable(R.drawable.viaplay_logo)));
    }

    @Test
    public void mainScreenHasContent_detailFragmentContainer() {
        onView(withId(R.id.detail_fragment_container)).check(matches(isDisplayed())).check(matches(notNullValue()));
    }

    // Remember to unregister resources when not needed to avoid malfunction.
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
