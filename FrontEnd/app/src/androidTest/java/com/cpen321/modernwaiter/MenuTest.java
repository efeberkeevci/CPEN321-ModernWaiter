package com.cpen321.modernwaiter;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cpen321.modernwaiter.testing.MockMainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MenuTest {

    @Before
    public void createActivity() {
        ActivityScenario.launch(MockMainActivity.class);
    }

    @Test
    public void checkMenuDisplay(){

        //check that the recommendation is displayed
        onView(withId(R.id.feature_name))
                .check(matches(isDisplayed()));
        onView(withId(R.id.feature_description))
                .check(matches(isDisplayed()));

        //check that the menu items list is displayed
        onView(withId(R.id.menu_recycler))
                .check(matches(isDisplayed()));

        //check that the viewCart button is displayed
        onView(withId(R.id.viewCartButton))
                .check(matches(withText("View Cart")));
    }

    @Test
    public void checkMenuItemDetail(){
        //make sure that recycler view is displayed
        onView(withId(R.id.menu_recycler))
                .check(matches(isDisplayed()));

        //click on a menu item
        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check if the menu item detail view is displayed
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));
        onView(withId(R.id.topCardView))
                .check(matches(isDisplayed()));
    }
}
