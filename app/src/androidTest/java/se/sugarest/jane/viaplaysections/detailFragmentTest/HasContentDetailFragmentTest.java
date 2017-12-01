package se.sugarest.jane.viaplaysections.detailFragmentTest;

import android.content.Intent;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.ui.MainActivity;
import se.sugarest.jane.viaplaysections.ui.detail.DetailFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.IdlingRegistry.getInstance;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by jane on 17-12-1.
 */
@RunWith(AndroidJUnit4.class)
public class HasContentDetailFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private Fragment fragment;
    private MainActivity mainActivity;

    private IdlingResource mIdlingResource;

    @Before
    public void setUp() {
        // register IdlingResource
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        getInstance().register(mIdlingResource);
        // DetailFragment
        fragment = new DetailFragment();
    }

    @Test
    public void DetailFragmentHasContent() {
        mainActivity = mActivityTestRule.launchActivity(new Intent());
        mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_fragment_container, fragment)
                .commit();

        // onView(withId(R.id.section_title_label_text_view)).check(matches(isDisplayed())).check(matches(notNullValue()));

        onView(withId(R.id.section_title_content_text_view)).check(matches(isDisplayed())).check(matches(notNullValue()));

        // onView(withId(R.id.section_description_label_text_view)).check(matches(isDisplayed())).check(matches(notNullValue()));

        onView(withId(R.id.section_description_content_text_view)).check(matches(isDisplayed())).check(matches(notNullValue()));
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            getInstance().unregister(mIdlingResource);
        }
    }
}
