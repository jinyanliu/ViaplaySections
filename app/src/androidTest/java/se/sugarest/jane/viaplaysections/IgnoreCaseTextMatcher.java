package se.sugarest.jane.viaplaysections;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;

/**
 * Created by jane on 17-11-16.
 */
public class IgnoreCaseTextMatcher {
    /**
     * Returns a matcher that matches {@link TextView} based on its text property value BUT IGNORES CASE.
     *
     * @param text {@link String} with the text to match
     */
    public static Matcher<View> withText(String text) {
        return withText(is(text.toLowerCase()));
    }

    private static Matcher<View> withText(final Matcher<String> stringMatcher) {
        checkNotNull(stringMatcher);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text (ignoring case): ");
                stringMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(TextView textView) {
                return stringMatcher.matches(textView.getText().toString().toLowerCase());
            }
        };
    }
}
