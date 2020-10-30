package com.cpen321.modernwaiter;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*  Contains all the data required for this table's session such as its
    cart & the restaurant's menu.

    Note that we might change underlying datastructure & thats why we have
    the getter methods.
 */
public class TableSession {

    // This is a map of how many items are already ordered in the server
    private HashMap<MenuItem, Integer> orderedItems;

    // A list of Menu's items. Each menu's item will
    // its common attribute and also a field called quantity.
    // Quantity is the amount that has is in a user's cart
    // not to be mistaken with the items that are already ordered in the backend
    // Once an order is sent to the backend, it cannot be cancelled
    private ArrayList<MenuItem> menuItems;

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

        fetchMenu();
        postOrderId();
        getUserRecommendation();
    }

    private void fetchMenu() {

        LinkedList<MenuItem> response_menu_items = new LinkedList<MenuItem>();
        String url ="http://52.188.158.129:3000/items/1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<MenuItem> newMenuItems = new Gson().fromJson(response, new TypeToken<List<MenuItem>>() {}.getType());

                        for (MenuItem newMenuItem : newMenuItems) {
                            if (!menuItems.contains(newMenuItem)) {
                                menuItems.add(newMenuItem);
                                orderedItems.put(newMenuItem, 0);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Fetch Menu", error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getUserRecommendation() {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("users_id", HARDCODED.USER_ID);
        bodyFields.put("restaurant_id", HARDCODED.RESTAURANT_ID);

        final String bodyJSON = new Gson().toJson(bodyFields);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                HARDCODED.URL + "item/recommend",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        FeatureResponse featureResponse = new Gson().fromJson(response, FeatureResponse.class);

                        for (MenuItem menuItem : menuItems) {
                            if (menuItem.id == featureResponse.itemId);
                                featureItem = menuItem;
                        }

                        updateMenuFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
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
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getOrderId();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
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
        //
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                HARDCODED.URL + "order/user/" + HARDCODED.USER_ID + "?isActive=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<OrderResponse> orderResponse = new Gson().fromJson(response, new TypeToken<List<OrderResponse>>() {}.getType());

                        orderId = orderResponse.get(0).id;
                        updateBill();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    public void updateBill() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                HARDCODED.URL + "ordered-items/" + orderId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<OrderedItemResponse> orderedItemResponses = new Gson().fromJson(response, new TypeToken<List<OrderedItemResponse>>() {
                        }.getType());
                        HashMap<MenuItem, Integer> updatedBillMap = new HashMap<MenuItem, Integer>(menuItems.stream().collect(Collectors.toMap(
                                Function.identity(), x -> 0
                        )));

                        for (OrderedItemResponse orderedItem : orderedItemResponses) {
                            MenuItem fakeMenuItem = new MenuItem(orderedItem.items_id);
                            updatedBillMap.replace(fakeMenuItem, updatedBillMap.get(fakeMenuItem) + 1);
                        }

                        for (MenuItem menuItem : updatedBillMap.keySet()) {
                            orderedItems.replace(menuItem, updatedBillMap.get(menuItem));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }
        );

        requestQueue.add(stringRequest);
    }

    public void addMenuItems(ArrayList<MenuItem> menuItems) {
        for(MenuItem item : menuItems){
            item.quantity = "0";
            this.menuItems.add(item);
            orderedItems.put(item, 0);
        }
        updateMenuFragment();
    }

    //remove all items from cart
    public void checkout() {
        // Post /ordered-item/order:id forever
        // ITEMS IN THE ORDERCART BASED ON ITS MENUITEM.QUANTITY
        // Set is active to 2
        if (orderId == -1) {
            return;
        }

        for (MenuItem menuItem : menuItems) {

            if(Integer.parseInt(menuItem.quantity) > 0) {
                StringRequest stringRequest = createPostOrder(menuItem);

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

    // Get the list of all items in the menu
    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    // Return a map of MenuItems to the quantity ordered in the backend
    public HashMap<MenuItem, Integer> getBill() {
        return orderedItems;
    }

    public HashMap<MenuItem, Integer> getCart() {
        return new HashMap<>(menuItems.stream()
                .collect(
                        Collectors.toMap(x -> x, MenuItem::getIntegerQuantity)
                ));
    }

    public MenuItem getFeatureItem() {
        return featureItem;
    }

    public StringRequest createPostOrder(MenuItem menuItem) {
        HashMap<String, String> bodyFields = new HashMap<>();
        bodyFields.put("orderId", String.valueOf(orderId));
        bodyFields.put("itemId", String.valueOf(menuItem.id));

        final String bodyJSON = new Gson().toJson(bodyFields);
        return new StringRequest(
                Request.Method.POST,
                HARDCODED.URL + "ordered-items",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            System.out.println("Success");
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
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
        // TODO:
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
        System.out.println(navController.getCurrentDestination());
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
