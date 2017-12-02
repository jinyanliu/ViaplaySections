package se.sugarest.jane.viaplaysections.listFragmentTest;

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

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.MainActivity;
import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.ui.list.ListFragment;
import se.sugarest.jane.viaplaysections.ui.list.SectionAdapter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.IdlingRegistry.getInstance;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static se.sugarest.jane.viaplaysections.util.IgnoreCaseTextMatcher.withText;

/**
 * Created by jane on 17-12-1.
 */
@RunWith(AndroidJUnit4.class)
public class RecyclerViewListFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private Fragment fragment;
    private MainActivity mainActivity;
    private SectionAdapter sectionAdapter;
    private ArrayList<String> sectionTitlesStrings = new ArrayList<>();
    private IdlingResource mIdlingResource;

    @Before
    public void setUp() {

        // register IdlingResource
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        getInstance().register(mIdlingResource);

        // ListFragment
        fragment = new ListFragment();

        // sectionAdapter
        sectionAdapter = new SectionAdapter(null);
    }

    @Test
    public void clickNavigationMenuItem_DataChangeOnMainScreen() {

        mainActivity = mActivityTestRule.launchActivity(new Intent());

        mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navigation_drawer_container, fragment)
                .commit();

        String firstSection = "Serier";
        String secondSection = "Film";
        sectionTitlesStrings.add(firstSection);
        sectionTitlesStrings.add(secondSection);

        sectionAdapter.setUpTitleStringArray(sectionTitlesStrings);

        onView(withId(R.id.navigation_menu)).perform(click());

        onView(withId(R.id.navigation_drawer_container)).check(matches(isDisplayed()));

        // Position index starts from 0
        onView(withId(R.id.navigation_drawer_recycler_view)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.title_on_the_app_bar)).check(matches(notNullValue()))
                .check(matches(withText(secondSection)));
    }

    // Unregister IdlingResource to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            getInstance().unregister(mIdlingResource);
        }
    }
}
