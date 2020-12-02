package com.cpen321.modernwaiter.customer.application;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.BuildConfig;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.customer.ui.payment.peritem.PaymentItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
    private final ArrayList<PaymentItem> orderList = new ArrayList<>();

    private List<String> choices = new ArrayList<>();
    public final RequestQueue requestQueue;

    private MenuItem featureItem;

    public int orderId = -1;

    private final AppCompatActivity activity;

    public boolean isActive = true;

    // Testing values, change later
    private final String restaurantId;
    private final String tableId;
    private int userId;

    private HashSet<String> userPreference;

    private final HashMap<Integer, String> customerIdToName = new HashMap<>();

    private final Bundle accountBundle;

    //creates a new session
    TableSession(RequestQueue requestQueue, AppCompatActivity activity, Bundle accountBundle) {
        //Make request to server to retrieve menu items to display
        this.activity = activity;
        this.requestQueue = requestQueue;
        this.accountBundle = accountBundle;

        restaurantId = accountBundle.getString("restaurantId");
        tableId = accountBundle.getString("tableId");
        userId = accountBundle.getInt("userId");

        orderedItems = new HashMap<>();

        customerIdToName.put(-1, "Not selected");

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
        // TODO: REACTITVATE THIS
        //isActive = false;
        //NotificationService.unsubscribe(String.valueOf(orderId));
        navigateToPostPayment();
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


    public ArrayList<PaymentItem> getOrderList() {
        return orderList;
    }

    @Override
    public int getUserCount() {
        return customerIdToName.keySet().size();
    }

    @Override
    public void updateItemSelected(PaymentItem orderItem) {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("orderId", String.valueOf(orderId));
        bodyFields.put("itemId", String.valueOf(orderItem.menuItem.id));
        bodyFields.put("isSelected", orderItem.selected ? "1" : "0");
        bodyFields.put("userId", orderItem.selected ? "" + userId : "-1");

        final String bodyJSON = new Gson().toJson(bodyFields);
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, ApiUtil.orderSelect,
                response -> System.out.println("Success"),

                error -> Log.i("Select Item", error.toString())
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

        ArrayList<PostMenuItem> postMenuItems = new ArrayList<>();

        // Make a request for each order
        orderedItems.forEach(((menuItem, count) -> {
            if (menuItem.getIntegerQuantity() > 0) {
                PostMenuItem postMenuItem = new PostMenuItem(orderId, menuItem.id);

                for (int i = 0; i < Integer.parseInt(menuItem.quantity); i++) {
                    postMenuItems.add(postMenuItem);
                }

                // Add those value into orderedItems
                orderedItems.replace(menuItem, count + menuItem.getIntegerQuantity());
            }

            // Clear cart of all items
            menuItem.quantity = "0";
        }));

        final Map<String, Object> bodyFields = new HashMap<>();
        bodyFields.put("ordered_item_array", postMenuItems);
        bodyFields.put("userId", "" + userId);

        final String bodyJSON = new Gson().toJson(bodyFields);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUtil.orderedItems,
                response -> {
                    Log.i("MSG:",response);

                }, error -> Log.i("ERR:", error.toString())
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

    @Override
    public void add(Request request) {
        requestQueue.add(request);
    }

    public void fetchMenu() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiUtil.items + restaurantId,
                response -> {
                    ArrayList<MenuItem> newMenuItems = new Gson().fromJson(response, new TypeToken<List<MenuItem>>() {}.getType());

                    for (MenuItem newMenuItem : newMenuItems) {
                        if (!getMenuItems().contains(newMenuItem)) {
                            newMenuItem.quantity = "0";
                            orderedItems.put(newMenuItem, 0);
                        }
                    }

                    fetchOrderList();
                    fetchUserRecommendation();
                }, error -> Log.i("Fetch Menu", error.toString()));

        requestQueue.add(stringRequest);
    }

    private void postOrderId() {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("userId", "" + userId);
        bodyFields.put("tableId", tableId);
        bodyFields.put("restaurantId", restaurantId);
        bodyFields.put("amount", "0");
        bodyFields.put("hasPaid", "0");
        bodyFields.put("isActive", "1");

        final String bodyJSON = new Gson().toJson(bodyFields);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUtil.order,
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
                Request.Method.GET, ApiUtil.orderTable + tableId + ApiUtil.isActive,
                response -> {
                    List<OrderResponse> orderResponses = new Gson().fromJson(response, new TypeToken<List<OrderResponse>>() {
                    }.getType());

                    if (orderResponses.size() != 0) {
                        OrderResponse currentOrder = orderResponses.get(orderResponses.size() - 1);
                        if (BuildConfig.DEBUG && !(currentOrder.restaurant_id.equals(ApiUtil.RESTAURANT_ID))) {
                            throw new AssertionError("Restaurant ID invalid, restaurandid: " + restaurantId + "\n tableId: " + tableId);
                        }

                        orderId = currentOrder.id;

                        fetchMenu();
                        NotificationService.sendToken(String.valueOf(orderId));
                        return;
                    }
                    postOrderId();

                }, error -> Log.i("Fetch order id", error.toString()));

        requestQueue.add(stringRequest);
    }

    public void fetchOrderList() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiUtil.orderedItems + orderId,
                response -> {
                    List<OrderedItemResponse> orderedItemResponses = new Gson().fromJson(response,
                            new TypeToken<List<OrderedItemResponse>>() {}.getType());
                    orderList.clear();

                    HashMap<MenuItem, Integer> updatedBillMap = new HashMap<>();

                    for (OrderedItemResponse orderedItem : orderedItemResponses) {
                        // Put the username into our customer list
                        if (!customerIdToName.containsKey(orderedItem.users_id)) {
                            customerIdToName.put(orderedItem.users_id, "Loading...");
                            fetchUserInfo(orderedItem.users_id);
                        }

                        if (orderedItem.has_paid != 1) {

                            MenuItem fakeMenuItem = new MenuItem(orderedItem.items_id);

                            for (MenuItem menuItem : orderedItems.keySet()) {
                                if (menuItem.equals(fakeMenuItem)) {
                                    orderList.add(new PaymentItem(menuItem, orderedItem.is_selected == 1, orderedItem.users_id));
                                }
                            }

                            if (updatedBillMap.containsKey(fakeMenuItem))
                                updatedBillMap.replace(fakeMenuItem, updatedBillMap.get(fakeMenuItem) + 1);
                            else
                                updatedBillMap.put(fakeMenuItem, 1);
                        }
                    }
                    Collections.sort(orderList);

                    for (MenuItem menuItem : getBill().keySet()) {
                        if (updatedBillMap.containsKey(menuItem))
                            orderedItems.replace(menuItem, updatedBillMap.get(menuItem));
                        else orderedItems.replace(menuItem, 0);
                    }

                    refreshBillFragment();
                    refreshOrderListFragment();
                }, error -> Log.i("Fetch order", error.toString())
        );

        requestQueue.add(stringRequest);
    }

    public void fetchUserInfo(int userId) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiUtil.users + userId,
                response -> {
                    if (response.equals("")) {
                        Log.i("Fetch user info", "No user found for this id");
                    } else {
                        UserResponse userResponse = new Gson().fromJson(response, new TypeToken<UserResponse>() {}.getType());
                        customerIdToName.replace(userId, userResponse.username);
                        refreshOrderListFragment();

                        if (userId == this.userId) {
                            userPreference = new HashSet<>(Arrays.asList(userResponse.preferences.split(" ")));
                            Log.i("Fetch user info", "Updated user preference");
                        }
                    }
                }, error -> Log.i("Fetch user info", error.toString()));

        requestQueue.add(stringRequest);
    }

    public void fetchUserRecommendation() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiUtil.recommend + userId + "/" + restaurantId,
                response -> {

                    FeatureResponse featureResponses = new Gson().fromJson(response, FeatureResponse.class);

                    for (MenuItem menuItem : getMenuItems()) {
                        if (menuItem.id == featureResponses.itemId)
                            featureItem = menuItem;
                    }

                    refreshMenuFragment();
                },

                error -> Log.i("Fetch User recommendation", error.toString())
        );

        requestQueue.add(stringRequest);
    }

    @Override
    public HashSet<String> getUserPreference() {
        return userPreference;
    }

    public String getUsernameFromId(int id) {
        return customerIdToName.get(id);
    }

    public int getUserId() {
        return userId;
    }

    private void refreshMenuFragment() {
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);

        if(navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() == R.id.navigation_menu)

            navController.navigate(R.id.action_navigation_refresh_menu);
    }

    private void refreshBillFragment() {
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);

        if(navController.getCurrentDestination() != null) {
            switch (navController.getCurrentDestination().getId()) {
                case R.id.navigation_bill:
                    navController.navigate(R.id.action_navigation_refresh_bill);
                    break;
                case R.id.navigation_per_item_payment:
                    navController.navigate(R.id.action_navigation_refresh_per_item_payment);
                    break;
                default:
                    break;
            }
        }
    }

    private void refreshOrderListFragment() {
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);

        if(navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() == R.id.navigation_per_item_payment)

            navController.navigate(R.id.action_navigation_refresh_per_item_payment);
    }

    private void navigateToPostPayment() {
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);

        if(navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() == R.id.navigation_stripe)

            navController.navigate(R.id.action_navigation_stripe_to_navigation_post_payment);
    }

    public class OrderResponse {
        public int id;
        public String restaurant_id;
    }

    public class FeatureResponse {
        public int itemId;
    }

    public class OrderedItemResponse {
        public int items_id;
        public int is_selected;
        public int has_paid;
        public int users_id;
    }

    public class PostMenuItem {
        public int orderId;
        public int itemId;

        public PostMenuItem(int orderId, int itemId) {
            this.orderId = orderId;
            this.itemId = itemId;
        }
    }

    public class UserResponse {
        public String username;
        public String preferences;
        public int id;
    }
}
