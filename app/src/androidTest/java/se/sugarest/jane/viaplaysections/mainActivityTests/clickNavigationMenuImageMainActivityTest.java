//package se.sugarest.jane.viaplaysections.mainActivityTests;
//
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import se.sugarest.jane.viaplaysections.R;
//import se.sugarest.jane.viaplaysections.ui.MainActivity;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//
///**
// * Created by jane on 17-11-21.
// */
//@RunWith(AndroidJUnit4.class)
//public class clickNavigationMenuImageMainActivityTest {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityTestRule
//            = new ActivityTestRule<>(MainActivity.class);
//
//    @Test
//    public void clickNavigationMenuImage_OpensDrawerNavigation() {
//        onView(withId(R.id.navigation_menu))
//                .perform(click());
//        onView(withId(R.id.left_drawer)).check(matches(isDisplayed()));
//    }
//}
