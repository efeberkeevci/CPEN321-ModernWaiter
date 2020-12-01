package com.cpen321.modernwaiter;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.cpen321.modernwaiter.customer.application.CustomerActivity;
import com.cpen321.modernwaiter.customer.testing.MockCustomerActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class OrderCartTest {
    @Rule
    public ActivityScenarioRule<MockCustomerActivity> activityRule
            = new ActivityScenarioRule<>(MockCustomerActivity.class);
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

