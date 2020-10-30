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
import java.util.function.Function;
import java.util.stream.Collectors;

/*  Contains all the data required for this table's session such as its
    cart & the restaurant's menu.

    Note that we might change underlying datastructure & thats why we have
    the getter methods.
 */
public class TableSession {

    // This is a map of how many items are already ordered in the server
    // The amount of items on the cart is recorded in MenuItem.quantity
    private HashMap<MenuItem, Integer> orderedItems;

    private final RequestQueue requestQueue;

    private MenuItem featureItem;

    public int orderId = -1;

    private final AppCompatActivity activity;

    public boolean isActive = true;

    //creates a new session
    TableSession(RequestQueue requestQueue, AppCompatActivity activity) {
        //Make request to server to retrieve menu items to display
        this.activity = activity;
        this.requestQueue = requestQueue;

        orderedItems = new HashMap<MenuItem, Integer>();

        fetchMenu();
        postOrderId();
    }

    // Get the list of all items in the menu
    public Set<MenuItem> getMenuItems() {
        return getBill().keySet();
    }

    // Return a map of MenuItems to the quantity ordered in the backend
    public HashMap<MenuItem, Integer> getBill() {
        return orderedItems;
    }

    // Return a hashmap of MenuItem & Its quantity integer
    public HashMap<MenuItem, Integer> getCart() {
        return new HashMap<>(getMenuItems().stream()
                .collect(
                        Collectors.toMap(x -> x, MenuItem::getIntegerQuantity)
                ));
    }

    public MenuItem getFeatureItem() {
        return featureItem;
    }

    // Post an order to the server for all items on the cart then remove it
    public void checkout() {
        // Post /ordered-item/order:id forever
        // ITEMS IN THE ORDERCART BASED ON ITS MENUITEM.QUANTITY
        // Set is active to 2
        if (orderId == -1) {
            return;
        }


        String url = HARDCODED.URL + "checkout";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    Log.i("MSG:",response);

                }, error -> Log.i("ERR:", error.toString()));

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);


        for (MenuItem menuItem : getMenuItems()) {

            if(Integer.parseInt(menuItem.quantity) > 0) {
                stringRequest = createPostOrder(menuItem);

                for (int i = 0; i < Integer.parseInt(menuItem.quantity); i++) {
                    requestQueue.add(stringRequest);
                }

                // Add those value into orderedItems
                orderedItems.replace(
                        menuItem, orderedItems.get(menuItem) + Integer.valueOf(menuItem.quantity)
                );
            }

            // Clear the cart of all orders
            menuItem.quantity = String.valueOf(0);
        }
    }

    private void fetchMenu() {

        String url = HARDCODED.URL + "items/" + HARDCODED.RESTAURANT_ID;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    ArrayList<MenuItem> newMenuItems = new Gson().fromJson(response, new TypeToken<List<MenuItem>>() {}.getType());

                    for (MenuItem newMenuItem : newMenuItems) {
                        if (!getMenuItems().contains(newMenuItem)) {
                            newMenuItem.quantity = "0";
                            orderedItems.put(newMenuItem, 0);
                        }

                        getUserRecommendation();
                    }
                }, error -> Log.i("Fetch Menu", error.toString()));

        requestQueue.add(stringRequest);
    }

    private void postOrderId() {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("users_id", HARDCODED.USER_ID);
        bodyFields.put("tables_id", HARDCODED.TABLE_ID);
        bodyFields.put("restaurant_id", HARDCODED.RESTAURANT_ID);
        bodyFields.put("amount", "0");
        bodyFields.put("has_paid", "0");
        bodyFields.put("is_active_session", "1");

        final String bodyJSON = new Gson().toJson(bodyFields);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                HARDCODED.URL + "order",

                response -> getOrderId(),

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

    private void getOrderId() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, HARDCODED.URL + "order/user/" + HARDCODED.USER_ID + "?isActive=1",
                response -> {
                    List<OrderResponse> orderResponse = new Gson().fromJson(response, new TypeToken<List<OrderResponse>>() {}.getType());

                    orderId = orderResponse.get(0).id;
                    updateBill();

                }, error -> Log.i("Fetch order id", error.toString()));

        requestQueue.add(stringRequest);
    }

    public void updateBill() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, HARDCODED.URL + "ordered-items/" + orderId,
                response -> {
                    List<OrderedItemResponse> orderedItemResponses = new Gson().fromJson(response, new TypeToken<List<OrderedItemResponse>>() {
                    }.getType());
                    HashMap<MenuItem, Integer> updatedBillMap = new HashMap<MenuItem, Integer>(getMenuItems().stream().collect(Collectors.toMap(
                            Function.identity(), x -> 0
                    )));

                    for (OrderedItemResponse orderedItem : orderedItemResponses) {
                        MenuItem fakeMenuItem = new MenuItem(orderedItem.items_id);
                        updatedBillMap.replace(fakeMenuItem, updatedBillMap.get(fakeMenuItem) + 1);
                    }

                    for (MenuItem menuItem : updatedBillMap.keySet()) {
                        orderedItems.replace(menuItem, updatedBillMap.get(menuItem));
                    }

                }, error -> Log.i("Fetch Bill", error.toString())
        );

        requestQueue.add(stringRequest);
    }

    public void getUserRecommendation() {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("users_id", HARDCODED.USER_ID);
        bodyFields.put("restaurant_id", HARDCODED.RESTAURANT_ID);

        final String bodyJSON = new Gson().toJson(bodyFields);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, HARDCODED.URL + "item/recommend",
                response -> {

                    FeatureResponse featureResponse = new Gson().fromJson(response, FeatureResponse.class);

                    for (MenuItem menuItem : getMenuItems()) {
                        if (menuItem.id == featureResponse.itemId)
                            featureItem = menuItem;
                    }

                    updateMenuFragment();
                },

                error -> Log.i("Fetch User recommendation", error.toString())
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

    public StringRequest createPostOrder(MenuItem menuItem) {
        HashMap<String, String> bodyFields = new HashMap<>();
        bodyFields.put("orderId", String.valueOf(orderId));
        bodyFields.put("itemId", String.valueOf(menuItem.id));

        final String bodyJSON = new Gson().toJson(bodyFields);
        return new StringRequest(
                Request.Method.POST, HARDCODED.URL + "ordered-items",
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

    public void updateMenuFragment() {
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);

        if(navController.getCurrentDestination().getId() == R.id.navigation_menu)
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
