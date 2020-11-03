package com.cpen321.modernwaiter;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cpen321.modernwaiter.application.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NavigationBarTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testNavigation() {
        //check if the navigation buttons can be clicked and navigate to the right view
        onView(withId(R.id.navigation_menu)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.navigation_order)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuToCart(){
        //view menu as default (start_destination)
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));

        //click on cart symbol in bottom navigation bar
        onView(withId(R.id.navigation_order))
                .perform(click());

        //check that the cart is displayed
        onView(withId(R.id.fragment_order))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCartToMenu(){
        //view cart
        onView(withId(R.id.navigation_order))
                .perform(click());

        //check that the cart is displayed
        onView(withId(R.id.fragment_order))
                .check(matches(isDisplayed()));

        //now click on menu in bottom navigation bar
        onView(withId(R.id.navigation_menu))
                .perform(click());

        //check that the menu is displayed
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));
    }
}
