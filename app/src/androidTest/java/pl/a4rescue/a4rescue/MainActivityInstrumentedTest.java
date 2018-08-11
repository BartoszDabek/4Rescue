package pl.a4rescue.a4rescue;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.a4rescue.a4rescue.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void buttonIsEnabled() {
        onView(withId(R.id.startBtn)).check(matches(isEnabled()));
    }

    @Test
    public void buttonIsDisplayed() {
        onView(withId(R.id.startBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void buttonIsCompletelyDisplayed() {
        onView(withId(R.id.startBtn)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void buttonIsNotSelectable() {
        onView(withId(R.id.startBtn)).check(matches(not(isSelected())));
    }

    @Test
    public void buttonIsClickable() {
        onView(withId(R.id.startBtn)).check(matches(isClickable()));
    }
    @Test
    public void buttonWithText() {
        onView(withId(R.id.startBtn)).check(matches(withText(R.string.start)));
    }

}
