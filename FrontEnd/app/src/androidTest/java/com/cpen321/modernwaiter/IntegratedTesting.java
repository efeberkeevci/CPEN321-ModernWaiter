package com.cpen321.modernwaiter;

import android.support.test.InstrumentationRegistry;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;


import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.cpen321.modernwaiter.customer.application.CustomerActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.android.volley.Request.Method.GET;
import static org.junit.Assert.assertEquals;

public class IntegratedTesting {

    private final static String NOTIFICATION_TITLE = "Order Received!";
    private final static String NOTIFICATION_TEXT = "Order Received";
    private final static int TIMEOUT = 50000;

    @Before
    public void changeUserAndTableId() throws InterruptedException {
        ApiUtil.TABLE_ID = "2";
        ApiUtil.USER_ID = "2";
        ActivityScenario.launch(CustomerActivity.class);
        Thread.sleep(1000);
    }

    /**
     * Check if the server is running
     */
    @Test public void checkServerConnection(){
        StringRequest stringRequest = new StringRequest(GET, ApiUtil.health, response -> {
            if(response == null) Assert.fail("Received null response from backend health checkup");
            else Assert.assertTrue( true);
        }, error -> {
            Assert.fail("Could not connect to the server" + error.toString());
        });
    }

    /**
     * add item to cart
     * view cart
     * check if the item was added to the cart
     * cleanup : remove item from cart
     */
    @Test
    public void addMenuItemToOrder(){
        try {
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

            Assert.assertTrue( true);
        }
        catch(Exception e){
            Assert.fail("************Could not add menu item to order*************\n" + e.getMessage());
        }

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
        try {
            onView(withId(R.id.menu_recycler))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            String addedItem = "Spicy Ahi Roll";

            //add item to cart
            onView(withId(R.id.incrementButton))
                    .perform(click());

            //now go back to menu
            onView(withId(R.id.exitButton))
                    .perform(click());
            try{
                onView(withId(R.id.viewCartButton))
                        .check(matches(isDisplayed()));
            } catch(Exception e){
                Assert.fail("View Cart Button is not displayed");
                return;
            }
            try {
                //click view cart
                onView(withId(R.id.viewCartButton))
                        .perform(click());
            }
            catch(Exception e){
                Assert.fail("Click ViewCartButton is not working");
                return;
            }
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

            Assert.assertTrue( true);

        } catch(Exception e){
            Assert.fail("***********Could not add item to the table order*************\n" + e.getMessage());
        }
    }

    /**TODO: write this test
     * check for push notification : order has been placed
     */
    @Test
    public void checkPushNotification(){

        //by default on menu
        try {

            try {
                //click on a menu item
                onView(withId(R.id.menu_recycler))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

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
            }
            catch(Exception e){
                Assert.fail("Something wrong with display");
            }
            try {
                UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
                device.openNotification();
                device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), TIMEOUT);
                UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
                UiObject2 text = device.findObject(By.text(NOTIFICATION_TEXT));
                assertEquals(NOTIFICATION_TITLE, title.getText());
                assertEquals(NOTIFICATION_TEXT, text.getText());
                title.click();
            }
            catch(Exception e){
                Assert.fail("Something wrong with device");
            }
        }
        catch(Exception e){
            Assert.fail("************Did not receive any notification regarding order********\n" +e.getMessage());
        }
    }


    /**
     * Pay for all
     */
    /**
     * view bill
     * initiate payment
     * pay for all
     * input details
     * pay
     * check if successful
     */
    @Test
    public void payForAll(){

        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

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


        //input payment details
        /*String creditCardNumber = "4242" + "4242" + "4242" + "4242";
        String date = "424";
        String cv = "012";
        String postal = "123";
        String info = creditCardNumber+date+cv;
        //input details
        onView(withId(R.id.cardInputWidget))
                .perform(typeText(info), closeSoftKeyboard());

        //press pay
        onView(withId(R.id.payButton))
                .perform(click());

        //check that payment was successful
        onView(withId(R.id.textView2))
                .check(matches(isDisplayed()));
        onView(withId(R.id.textView2))
                .check(matches(withText(R.string.thank_you_post_payment)));*/
    }




}
