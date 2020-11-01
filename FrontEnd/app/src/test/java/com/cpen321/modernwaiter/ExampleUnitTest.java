package com.cpen321.modernwaiter;


import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.test.ext.junit.rules.ActivityScenarioRule;


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
@LargeTest
public class ExampleUnitTest {

    @Rule
    public ActivityScenarioRule<TestActivity> activityRule =
            new ActivityScenarioRule<>(TestActivity.class);

    @Test
    public void listGoesOverTheFold() {

    }
}
