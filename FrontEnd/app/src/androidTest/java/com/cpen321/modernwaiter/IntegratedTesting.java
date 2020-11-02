package com.cpen321.modernwaiter;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cpen321.modernwaiter.application.MainActivity;
import com.cpen321.modernwaiter.ui.order.OrderRecyclerAdapter;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.util.EnumSet.allOf;

@RunWith(AndroidJUnit4.class)
public class IntegratedTesting {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void addMenuItemToOrder(){
        //view menu as default (start_destination)
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));

        //check that the menu items list is displayed
        onView(withId(R.id.menu_recycler))
                .check(matches(isDisplayed()));

        //make sure that recycler view is displayed
        onView(withId(R.id.menu_recycler))
                .check(matches(isDisplayed()));

        //click on a menu item
        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check if the menu item detail view is displayed
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));
        onView(withId(R.id.cardView))
                .check(matches(isDisplayed()));

        String addedItem = "Spicy Ahi Roll";
        /**
         * Now add this item to the cart
         */
        onView(withId(R.id.incrementButton))
                .perform(click());

        //now go back to menu
        onView(withId(R.id.exitButton))
                .perform(click());

        //check that in menu fragment
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));

        //click view cart
        onView(withId(R.id.viewCartButton))
                .perform(click());

        //check that cart is displayed
        onView(withId(R.id.fragment_order))
                .check(matches(isDisplayed()));

        //check that order_recycler is displayed
        onView(withId(R.id.order_recycler))
                .check(matches(isDisplayed()));

        //check that added item is displayed
        // Attempt to scroll to an item that contains the special text.
        onView(ViewMatchers.withId(R.id.order_recycler))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(addedItem))
                ));

        //////////cleanup///////////////////

        //now click on menu in bottom navigation bar
        onView(withId(R.id.navigation_menu))
                .perform(click());

        //click on a menu item
        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //remove the item
        onView(withId(R.id.decrementButton))
                .perform(click());

    }

}
