package se.sugarest.jane.viaplaysections;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.data.SectionAdapter;
import se.sugarest.jane.viaplaysections.ui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static se.sugarest.jane.viaplaysections.IgnoreCaseTextMatcher.withText;
import static se.sugarest.jane.viaplaysections.MainActivityTest.EspressoTestsMatchers.withDrawable;


/**
 * Created by jane on 17-11-16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private Context instrumentationCtx;
    private SectionAdapter sectionAdapter;
    private ArrayList<String> sectionTitlesStrings = new ArrayList<>();

    @Before
    public void setUp() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
        sectionAdapter = new SectionAdapter(null);
    }

    @Test
    public void mainScreenHasContent_menuImageOnTheAppBar() {
        onView(withId(R.id.navigation_menu)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withDrawable(R.drawable.ic_menu)));
    }

    @Test
    public void mainScreenHasContent_titleOnTheAppBar() {
        onView(withId(R.id.title_on_the_app_bar)).check(matches(isDisplayed()));
    }

    @Test
    public void mainScreenHasContent_viaplayLogoOnTheAppBar() {
        onView(withId(R.id.viaplay_logo)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withDrawable(R.drawable.viaplay_logo)));
    }

    @Test
    public void mainScreenHasContent_titleLabelTextView() {
        onView(withId(R.id.section_title_label)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withText(instrumentationCtx.getString(R.string.section_title_label))));
    }

    @Test
    public void mainScreenHasContent_titleContentTextView() {
        onView(withId(R.id.section_title)).check(matches(isDisplayed()));
    }

    @Test
    public void mainScreenHasContent_descriptionLabelTextView() {
        onView(withId(R.id.section_description_label)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withText(instrumentationCtx.getString(R.string.section_description_label))));
    }

    @Test
    public void mainScreenHasContent_descriptionContentTextView() {
        onView(withId(R.id.section_description)).check(matches(isDisplayed()));
    }

    @Test
    public void clickNavigationMenuImage_OpensDrawerNavigation() {
        onView(withId(R.id.navigation_menu))
                .perform(click());
        onView(withId(R.id.left_drawer)).check(matches(isDisplayed()));
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

    public static class EspressoTestsMatchers {

        public static Matcher<View> withDrawable(final int resourceId) {
            return new DrawableMatcher(resourceId);
        }

        public static Matcher<View> noDrawable() {
            return new DrawableMatcher(-1);
        }
    }
}
