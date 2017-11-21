package se.sugarest.jane.viaplaysections.MainActivityTests;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.data.SectionAdapter;
import se.sugarest.jane.viaplaysections.ui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static se.sugarest.jane.viaplaysections.IgnoreCaseTextMatcher.withText;

/**
 * Created by jane on 17-11-21.
 */
@RunWith(AndroidJUnit4.class)
public class RecyclerViewMainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private SectionAdapter sectionAdapter;
    private ArrayList<String> sectionTitlesStrings = new ArrayList<>();

    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Before
    public void setUp() {
        sectionAdapter = new SectionAdapter(null);
    }

    @Test
    public void clickNavigationMenuItem_DataChangeOnMainScreen() {
        String firstSection = "Serier";
        String secondSection = "Film";
        sectionTitlesStrings.add(firstSection);
        sectionTitlesStrings.add(secondSection);
        sectionAdapter.setUpTitleStringArray(sectionTitlesStrings);

        onView(withId(R.id.navigation_menu))
                .perform(click());

        // Position index starts from 0
        onView(withId(R.id.left_drawer))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.title_on_the_app_bar)).check(matches(notNullValue()))
                .check(matches(withText(secondSection)));
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
