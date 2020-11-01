package com.cpen321.modernwaiter;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cpen321.modernwaiter.application.MainActivity;
import com.cpen321.modernwaiter.testing.MockMainActivity;
import com.cpen321.modernwaiter.testing.MockTableSession;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MenuUsage {

    private MockTableSession mockTableSession;

    @Before
    public void createActivity() {
        ActivityScenario<MockMainActivity> activityScenario = ActivityScenario.launch(MockMainActivity.class);
        mockTableSession = (MockTableSession) MainActivity.tableSession;
    }

    @Test
    public void checkButton() {
        ViewInteraction view_cart = onView(withId(R.id.viewCartButton)).check(matches(withText("View Cart")));
    }
}
