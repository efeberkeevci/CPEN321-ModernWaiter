package com.cpen321.modernwaiter;

import com.android.volley.Request;
import com.cpen321.modernwaiter.application.MenuItem;
import com.cpen321.modernwaiter.application.SessionInterface;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Test tableSession, our fragment can still call these function
 * But they do not actually call the server in our UI test
 * Instead we can manually update the view in the table session
 * to ensure that the UI responds to state change
 *
 * Currently, not guaranteed to work with StripePayment since it handles callback differently
 */
public class TestTableSession implements SessionInterface {

    public HashMap<MenuItem, Integer> orderedItems;
    public MenuItem featureItem;
    public int updateBillCounter = 0;
    public int endSessionCounter = 0;
    public Request lastRequest;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int getOrderId() {
        return 0;
    }

    @Override
    public Set<MenuItem> getMenuItems() {
        return orderedItems.keySet();
    }

    @Override
    public HashMap<MenuItem, Integer> getBill() {
        return orderedItems;
    }

    @Override
    public HashMap<MenuItem, Integer> getCart() {
        return new HashMap<>(getMenuItems().stream()
                .collect(
                        Collectors.toMap(menuItem -> menuItem, MenuItem::getIntegerQuantity)
                ));
    }

    @Override
    public MenuItem getFeatureItem() {
        return featureItem;
    }

    @Override
    public void checkout() {
        orderedItems.forEach(((menuItem, count) -> {
            if (menuItem.getIntegerQuantity() > 0) {


                // Add those value into orderedItems
                orderedItems.replace(menuItem, count + menuItem.getIntegerQuantity());
            }

            // Clear cart of all items
            menuItem.quantity = "0";
        }));
    }

    @Override
    public void updateBill() {
        updateBillCounter++;
    }

    @Override
    public void add(Request request) {
        lastRequest = request;
    }

    @Override
    public void endSession() {
        endSessionCounter++;
    }
}
