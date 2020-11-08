package com.cpen321.modernwaiter;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cpen321.modernwaiter.application.ApiUtil;
import com.cpen321.modernwaiter.application.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class IntegratedTesting {

    @Before
    public void changeUserAndTableId() throws InterruptedException {
        ApiUtil.TABLE_ID = "2";
        ApiUtil.USER_ID = "2";
        ActivityScenario.launch(MainActivity.class);
        Thread.sleep(1000);
    }

    /**
     * add item to cart
     * view cart
     * check if the item was added to the cart
     * cleanup : remove item from cart
     */
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
        onView(withId(R.id.topCardView))
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
        onView(withId(R.id.order_recycler))
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

    /**
     * add item to cart
     * checkout
     * view bill
     * cleanup : done in next test payForAll
     */
    @Test
    public void addMenuItemAndViewBill() {
        //by default on menu

        //click on a menu item
        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        String addedItem = "Spicy Ahi Roll";

        //add item to cart
        onView(withId(R.id.incrementButton))
                .perform(click());

        //now go back to menu
        onView(withId(R.id.exitButton))
                .perform(click());

        //click view cart
        onView(withId(R.id.viewCartButton))
                .perform(click());

        //click on checkout
        onView(withId(R.id.checkoutButton))
                .perform(click());

        //click on view bill
        onView(withId(R.id.startBillButton))
                .perform(click());

        //check that its displaying the bill
        onView(withId(R.id.fragment_bill))
                .check(matches(isDisplayed()));

        //check that item is added to the bill
        onView(withId(R.id.bill_recycler))
                    // scrollTo will fail the test if no item matches.
                    .perform(RecyclerViewActions.scrollTo(
                            hasDescendant((withText(addedItem)))
                    ));


    }

    /**TODO: write this test
     * check for push notification : order has been placed
     */
    @Test
    public void checkPushNotification(){
        /*
        //by default on menu

        //click on a menu item
        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        String addedItem = "Dummy Roll";

        //add item to cart
        onView(withId(R.id.incrementButton))
                .perform(click());

        //now go back to menu
        onView(withId(R.id.exitButton))
                .perform(click());

        //click view cart
        onView(withId(R.id.viewCartButton))
                .perform(click());

        //click on checkout
        onView(withId(R.id.checkoutButton))
                .perform(click());
        */
        //TODO: not finished
    }

    /**
     * view bill
     * initiate payment
     * pay for all
     * input details
     * pay
     * check if successful
     */
    public void payForAll(){

        //by default on menu
        //click view cart
        onView(withId(R.id.viewCartButton))
                .perform(click());

        //click on view bill
        onView(withId(R.id.startBillButton))
                .perform(click());

        //check that its displaying the bill
        onView(withId(R.id.fragment_bill))
                .check(matches(isDisplayed()));


        /////////paying the bill///////////

        //initiate payment
        onView(withId(R.id.startPaymentButton))
                .perform(click());

        //check that on payment options page
        onView(withId(R.id.pay_for_all))
                .check(matches(isDisplayed()));

        //click on pay_for_all
        onView(withId(R.id.pay_for_all))
                .perform(click());

        //check that on stripe payment
        onView(withId(R.id.payButton))
                .check(matches(isDisplayed()));

        //click on pay
        onView(withId(R.id.payButton))
                .perform(click());

        //input payment details
        String creditCardNumber = "4242" + "4242" + "4242" + "4242";
        String date = "05/22";
        String cv = "012";
        String postal = "V3Z 8C7";
        //check that the widget is displayed
        onView(withId(R.id.cardInputWidget))
                .check(matches(isDisplayed()));
        //input details
        onView(withId(R.id.cardInputWidget))
                .perform(typeText(creditCardNumber+date+cv+postal), closeSoftKeyboard());
        //press pay
        onView(withId(R.id.payButton))
                .perform(click());

        //check that payment was successful
        onView(withId(R.id.textView2))
                .check(matches(isDisplayed()));
        onView(withId(R.id.textView2))
                .check(matches(withText(R.string.thank_you_post_payment)));
    }



}
