package com.cpen321.modernwaiter;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.cpen321.modernwaiter.customer.application.CustomerActivity;
import com.cpen321.modernwaiter.util.MyViewaction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.android.volley.Request.Method.GET;

public class PayTest {
    @Before
    public void changeUserAndTableId() throws InterruptedException {
        ApiUtil.notificationEnabled = false;

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CustomerActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt("userId", 2);
        bundle.putString("restaurantId", ApiUtil.RESTAURANT_ID);
        bundle.putString("tableId", "2");

        intent.putExtras(bundle);

        ActivityScenario.launch(intent);
        Thread.sleep(1000);
    }

    /**
     * Check if the server is running
     */
    @Test public void checkServerConnection(){
        new StringRequest(GET, ApiUtil.health, response -> {
            if(response == null) Assert.fail("Received null response from backend health checkup");
            else Assert.assertTrue( true);
        }, error -> {
            Assert.fail("Could not connect to the server" + error.toString());
        });
    }


    /**
     * Pay For All
     * view bill
     * initiate payment
     * pay for all
     * input details
     * pay
     * check if successful
     */
    @Test
    public void payForAll() throws InterruptedException {

        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //add item to cart
        onView(withId(R.id.incrementButton))
                .perform(click());

        Thread.sleep(300);
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

        Thread.sleep(600);
        //check that on payment options page
        onView(withId(R.id.payForAll))
                .check(matches(isDisplayed()));

        //click on pay_for_all
        onView(withId(R.id.payForAll))
                .perform(click());

        Thread.sleep(100);


        //input payment details
        String creditCardNumber = "4242" + "4242" + "4242" + "4242";
        String date = "0522";
        String cv = "012";
        String postal = "V3Z8C7";
        //check that the widget is displayed
        onView(withResourceName("card_number_edit_text"))
                .check(matches(isDisplayed()));

        onView(withResourceName("card_number_edit_text"))
                .perform(typeText(creditCardNumber+date+cv+postal), closeSoftKeyboard());
        //press pay
        onView(withId(R.id.payButton))
                .perform(click());

        Thread.sleep(2000);

        //check that payment was successful
        onView(withId(R.id.textView2))
                .check(matches(isDisplayed()));
        onView(withId(R.id.textView2))
                .check(matches(withText(R.string.thank_you_post_payment)));
    }

    @Test
    public void payPerItem() throws InterruptedException {

        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //add 1st item to cart
        onView(withId(R.id.incrementButton))
                .perform(click());

        //now go back to menu
        onView(withId(R.id.exitButton))
                .perform(click());

        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //add 2nd item to cart
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

        Thread.sleep(600);

        //click on pay_per_item
        onView(withId(R.id.demo_button))
                .perform(click());

        Thread.sleep(600);

        onView(withId(R.id.per_item_recycler))
                .check(matches(isDisplayed()));
        onView(withId(R.id.per_item_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewaction.clickChildViewWithId(R.id.checkbox)));

        Thread.sleep(1000);

        //check that the total amount displayed is 16.50 only (price of item1)
        onView(withId(R.id.payButton))
                .check(matches(isDisplayed()));

        String amount = "Pay $16.5";
        onView(withId(R.id.payButton))
                .check(matches(withText(amount)));

    }
}
