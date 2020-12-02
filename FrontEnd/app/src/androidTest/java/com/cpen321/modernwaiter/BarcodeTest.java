package com.cpen321.modernwaiter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import com.android.dx.command.Main;
import com.cpen321.modernwaiter.barcode.BarcodeActivity;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.cpen321.modernwaiter.customer.application.CustomerActivity;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class BarcodeTest {
    @Before
    public void changeUserAndTableId() throws InterruptedException {
        ApiUtil.notificationEnabled = false;

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);

        ActivityScenario.launch(intent);
        Thread.sleep(1000);
    }

    @Test
    public void barcode(){
        onView(withId(R.id.barcode_button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.barcode_button))
                .perform(click());
    }
}
