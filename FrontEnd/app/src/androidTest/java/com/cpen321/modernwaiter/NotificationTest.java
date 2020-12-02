package com.cpen321.modernwaiter;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.cpen321.modernwaiter.customer.application.CustomerActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.android.volley.Request.Method.GET;
import static org.junit.Assert.assertEquals;

public class NotificationTest {
    private final static String NOTIFICATION_TITLE = "Order Received!";
    private final static String NOTIFICATION_TEXT = "Order Received";
    private final static int TIMEOUT = 50000;
    @Before
    public void changeUserAndTableId() throws InterruptedException {
        ApiUtil.notificationEnabled = true;

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
    @Test
    public void checkServerConnection(){
        StringRequest stringRequest = new StringRequest(GET, ApiUtil.health, response -> {
            if(response == null) Assert.fail("Received null response from backend health checkup");
            else Assert.assertTrue( true);
        }, error -> {
            Assert.fail("Could not connect to the server" + error.toString());
        });
    }

    /**TODO: write this test
     * check for push notification : order has been placed
     */

    @Test
    public void checkPushNotification(){

        //by default on menu

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


        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.openNotification();

        device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), TIMEOUT);
        UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
        UiObject2 text = device.findObject(By.text(NOTIFICATION_TEXT));
        assertEquals(NOTIFICATION_TITLE, title.getText());
        assertEquals(NOTIFICATION_TEXT, text.getText());
        title.click();


    }


}
