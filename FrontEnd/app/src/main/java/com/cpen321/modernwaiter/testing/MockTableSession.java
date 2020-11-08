package com.cpen321.modernwaiter.testing;

import android.util.Log;

import com.android.volley.Request;
import com.cpen321.modernwaiter.application.MenuItem;
import com.cpen321.modernwaiter.application.SessionInterface;
import com.cpen321.modernwaiter.ui.payment.peritem.PaymentItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class MockTableSession implements SessionInterface {

    public HashMap<MenuItem, Integer> orderedItems = new HashMap<>(dummyOrderItem());
    public ArrayList<PaymentItem> orderList = new ArrayList<>();
    public int featureItemId = 1;
    public int updateBillCounter = 0;
    public int endSessionCounter = 0;
    public Request lastRequest;
    public int userCount = 1;

    /*public MockTableSession() {
    }*/

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
        for (MenuItem menuItem : getMenuItems()) {
            if (menuItem.id == featureItemId) {
                return menuItem;
            }
        }
        return null;
    }

    @Override
    public ArrayList<PaymentItem> getOrderList() {
        return orderList;
    }

    @Override
    public int getUserCount() {
        return userCount;
    }

    @Override
    public void updateItemSelected(PaymentItem orderItem) {
        Log.d("Alert:", "Inside updateItemSelected that doesn't do anything");
    }

    @Override
    public void checkout() {
        orderedItems.forEach(((menuItem, count) -> {
            if (menuItem.getIntegerQuantity() > 0) {

                for (int i = 0; i < menuItem.getIntegerQuantity(); i++)
                    orderList.add(new PaymentItem(menuItem, false));

                // Add those value into orderedItems
                orderedItems.replace(menuItem, count + menuItem.getIntegerQuantity());
            }

            // Clear cart of all items
            menuItem.quantity = "0";
        }));
    }

    @Override
    public void fetchBill() {
        updateBillCounter++;
    }

   @Override
    public void fetchOrderList() {
        Log.d("Alert:", "Inside fetchOrderList that doesn't do anything");
    }

    @Override
    public void add(Request request) {
        lastRequest = request;
    }

    @Override
    public void endSession() {
        endSessionCounter++;
    }

    public static Map<MenuItem, Integer> dummyOrderItem() {
        String json = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"restaurant_id\": 1,\n" +
                "    \"name\": \"Dummy Roll\",\n" +
                "    \"type\": \"Sushi\",\n" +
                "    \"cost\": 16.5,\n" +
                "    \"description\": \"ocean wise ahi tuna, mango, avocado, asparagus, cucumber, sesame soy paper, wasabi mayo, cripy yam curls\",\n" +
                "    \"calories\": 500,\n" +
                "    \"quantity\": 2,\n" +
                "    \"popularity_count\": 3,\n" +
                "    \"image\": \"gs://modern-waiter-47e96.appspot.com/dummy-spicy-ahi.jpg\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"restaurant_id\": 1,\n" +
                "    \"name\": \"Dummy Crunch\",\n" +
                "    \"type\": \"Sushi\",\n" +
                "    \"cost\": 16,\n" +
                "    \"description\": \"crispy prawn, mango, avocado, asparagus, cucumber, sesame soy paper, sriracha mayo, soy glaze\",\n" +
                "    \"calories\": 500,\n" +
                "    \"quantity\": 0,\n" +
                "    \"popularity_count\": 4,\n" +
                "    \"image\": \"gs://modern-waiter-47e96.appspot.com/dummy-prawn-crunch.jpg\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"restaurant_id\": 1,\n" +
                "    \"name\": \"Dummy Appies\",\n" +
                "    \"type\": \"Appetizers\",\n" +
                "    \"cost\": 18.5,\n" +
                "    \"description\": \"ocean wise lois lake steelhead, sustainably harvested prawns, avocado, chili, thai basil, mint, peruvian leche de tigre marinade\",\n" +
                "    \"calories\": 750,\n" +
                "    \"quantity\": 1,\n" +
                "    \"popularity_count\": 2,\n" +
                "    \"image\": \"gs://modern-waiter-47e96.appspot.com/dummy-prawn-crunch.jpg\"\n" +
                "  }\n" +
                "]";

        ArrayList<MenuItem> newMenuItems = new Gson().fromJson(json, new TypeToken<List<MenuItem>>() {}.getType());

        return newMenuItems.stream()
                .collect(Collectors.toMap(
                        menuItem -> menuItem, x -> 0
                ));
    }
}
