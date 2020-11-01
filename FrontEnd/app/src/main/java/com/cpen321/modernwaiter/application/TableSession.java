package com.cpen321.modernwaiter.application;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*  Contains all the data required for this table's session such as its
    cart & the restaurant's menu.

    Note that we might change underlying datastructure & thats why we have
    the getter methods.
 */
public class TableSession implements SessionInterface {

    // This is a map of how many items are already ordered in the server
    // The amount of items on the cart is recorded in MenuItem.quantity
    private final HashMap<MenuItem, Integer> orderedItems;

    public final RequestQueue requestQueue;

    private MenuItem featureItem;

    public int orderId = -1;

    private final AppCompatActivity activity;

    public boolean isActive = true;

    // Testing values, change later
    private final String restaurantId = API.RESTAURANT_ID;
    private final String tableId = API.TABLE_ID;
    private final String userId = API.USER_ID;

    //creates a new session
    TableSession(RequestQueue requestQueue, AppCompatActivity activity) {
        //Make request to server to retrieve menu items to display
        this.activity = activity;
        this.requestQueue = requestQueue;

        orderedItems = new HashMap<>();

        fetchMenu();
        fetchOrderId();
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public int getOrderId() {
        return orderId;
    }

    @Override
    public void endSession() {
        isActive = false;
    }

    // Get the list of all items in the menu
    @Override
    public Set<MenuItem> getMenuItems() {
        return getBill().keySet();
    }

    // Return a map of MenuItems to the quantity ordered in the backend
    @Override
    public HashMap<MenuItem, Integer> getBill() {
        return orderedItems;
    }

    // Return a hashmap of MenuItem & Its quantity integer
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

    // Post an order to the server for all items on the cart then remove it
    @Override
    public void checkout() {
        // Post /ordered-item/order:id forever
        // ITEMS IN THE ORDERCART BASED ON ITS MENUITEM.QUANTITY
        // Set is active to 2
        if (orderId == -1) {
            return;
        }

        String url = API.checkout;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    Log.i("MSG:",response);

                }, error -> Log.i("ERR:", error.toString()));

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

        orderedItems.forEach(((menuItem, count) -> {
            if (menuItem.getIntegerQuantity() > 0) {
                StringRequest stringRequest1 = createPostOrder(menuItem);

                for (int i = 0; i < Integer.parseInt(menuItem.quantity); i++) {
                    requestQueue.add(stringRequest1);
                }

                // Add those value into orderedItems
                orderedItems.replace(menuItem, count + menuItem.getIntegerQuantity());
            }

            // Clear cart of all items
            menuItem.quantity = "0";
        }));
    }

    @Override
    public void add(Request request) {
        requestQueue.add(request);
    }

    public void fetchMenu() {

        String url = API.items + restaurantId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    ArrayList<MenuItem> newMenuItems = new Gson().fromJson(response, new TypeToken<List<MenuItem>>() {}.getType());

                    for (MenuItem newMenuItem : newMenuItems) {
                        if (!getMenuItems().contains(newMenuItem)) {
                            newMenuItem.quantity = "0";
                            orderedItems.put(newMenuItem, 0);
                        }
                    }

                    getUserRecommendation();
                }, error -> Log.i("Fetch Menu", error.toString()));

        requestQueue.add(stringRequest);
    }

    private void postOrderId() {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("userId", userId);
        bodyFields.put("tableId", tableId);
        bodyFields.put("restaurantId", restaurantId);
        bodyFields.put("amount", "0");
        bodyFields.put("hasPaid", "0");
        bodyFields.put("isActive", "1");

        final String bodyJSON = new Gson().toJson(bodyFields);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, API.order,
                response -> fetchOrderId(),

                error -> Log.i("Post order", error.toString())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return bodyJSON.getBytes();
            }
        };

        requestQueue.add(stringRequest);
    }

    private void fetchOrderId() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, API.userOrder + userId + API.isActive,
                response -> {
                    List<OrderResponse> orderResponse = new Gson().fromJson(response, new TypeToken<List<OrderResponse>>() {}.getType());
                    if (orderResponse.size() == 0) {
                        postOrderId();
                    } else {
                        orderId = orderResponse.get(0).id;
                        updateBill();
                    }

                }, error -> Log.i("Fetch order id", error.toString()));

            requestQueue.add(stringRequest);
    }

    public void updateBill() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, API.orderedItems + orderId,
                response -> {
                    List<OrderedItemResponse> orderedItemResponses = new Gson().fromJson(response, new TypeToken<List<OrderedItemResponse>>() {
                    }.getType());
                    HashMap<MenuItem, Integer> updatedBillMap = new HashMap<>();

                    for (OrderedItemResponse orderedItem : orderedItemResponses) {
                        MenuItem fakeMenuItem = new MenuItem(orderedItem.items_id);

                        if (updatedBillMap.containsKey(fakeMenuItem))
                            updatedBillMap.replace(fakeMenuItem, updatedBillMap.get(fakeMenuItem) + 1);
                        else
                            updatedBillMap.put(fakeMenuItem, 1);
                    }

                    for (MenuItem menuItem : updatedBillMap.keySet()) {
                        orderedItems.replace(menuItem, updatedBillMap.get(menuItem));
                    }

                }, error -> Log.i("Fetch Bill", error.toString())
        );

        requestQueue.add(stringRequest);
    }

    private void getUserRecommendation() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, API.recommend + userId + "/" + restaurantId,
                response -> {

                    FeatureResponse featureResponses = new Gson().fromJson(response, FeatureResponse.class);

                    for (MenuItem menuItem : getMenuItems()) {
                        if (menuItem.id == featureResponses.itemId)
                            featureItem = menuItem;
                    }

                    updateMenuFragment();
                },

                error -> Log.i("Fetch User recommendation", error.toString())
        );

        requestQueue.add(stringRequest);
    }

    private StringRequest createPostOrder(MenuItem menuItem) {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("orderId", String.valueOf(orderId));
        bodyFields.put("itemId", String.valueOf(menuItem.id));

        final String bodyJSON = new Gson().toJson(bodyFields);
        return new StringRequest(
                Request.Method.POST, API.orderedItems,
                response -> System.out.println("Success"),

                error -> Log.i("Post order", error.toString())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return bodyJSON.getBytes();
            }
        };
    }

    private void updateMenuFragment() {
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);

        if(navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() == R.id.navigation_menu)

            navController.navigate(R.id.action_navigation_menu_to_navigation_menu);
    }

    public class OrderResponse {
        public int id;
    }

    public class FeatureResponse {
        public int itemId;
    }

    public class OrderedItemResponse {
        public int items_id;
    }
}
