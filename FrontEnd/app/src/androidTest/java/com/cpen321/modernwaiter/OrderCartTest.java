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
public class OrderCartTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testCartButtons(){

        //open cart
        onView(withId(R.id.navigation_order))
                .perform(click());

        //check that the cart is displayed
        onView(withId(R.id.fragment_order))
                .check(matches(isDisplayed()));

        //Checkout
        onView(withId(R.id.checkoutButton))
                .perform(click());

        //now the order has been placed

        //View Bill
        onView(withId(R.id.startBillButton))
                .perform(click());

        //check that the bill is displayed
        onView(withId(R.id.fragment_bill))
                .check(matches(isDisplayed()));

    }
}

