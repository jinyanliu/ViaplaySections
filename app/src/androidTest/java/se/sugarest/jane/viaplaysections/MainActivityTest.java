package se.sugarest.jane.viaplaysections;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.viaplaysections.ui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by jane on 17-11-16.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickMenuButton_OpensDrawerNavigation() {
        // Find the view and Perform action on the view
        onView(withId(R.id.button_navigation))
                .perform(click());
        onView(withId(R.id.left_drawer)).check(matches(isDisplayed()));
    }

    @Test
    public void mainScreenHasContent() {
        onView(withId(R.id.section_title_label)).check(matches(withText(R.string.section_title_label)));
        onView(withId(R.id.section_description_label)).check(matches(withText(R.string.section_description_label)));
        onView(withId(R.id.section_title)).check(matches(notNullValue()));
        onView(withId(R.id.section_description)).check(matches(notNullValue()));
        onView(withId(R.id.title_on_the_app_bar)).check(matches(notNullValue()));
    }


}
