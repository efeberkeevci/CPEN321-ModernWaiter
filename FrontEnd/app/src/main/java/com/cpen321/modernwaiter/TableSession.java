package com.cpen321.modernwaiter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/*  Contains all the data required for this table's session such as its
    cart & the restaurant's menu.

    Note that we might change underlying datastructure & thats why we have
    the getter methods.
 */
public class TableSession {

    // This is a
    private HashMap<MenuItem, Integer> orderedItems;

    // A list of Menu's items. Each menu's item will
    // its common attribute and also a field called quantity.
    // Quantity is the amount that has is in a user's cart
    // not to be mistaken with the items that are already ordered in the backend
    // Once an order is sent to the backend, it cannot be cancelled
    private ArrayList<MenuItem> menuItems;

    private final RequestQueue requestQueue;

    //creates a new session
    TableSession(RequestQueue requestQueue) {

        // TODO: Initialize the menu
        //Make request to server to retrieve menu items to display

        menuItems = MainActivity.menu_items;

        orderedItems = menuItems.stream().collect(
                Collectors.toMap(x -> x, x -> 0, (s, a) -> s, HashMap::new)
        );

        this.requestQueue = requestQueue;

        postOrderId();
        getUserRecommendation();
        // TODO: GET ORDERID order/user/user:id?isActive=1

    }

    public void getUserRecommendation() {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("userId", HARDCODED.USER_ID);
        bodyFields.put("restaurantId", HARDCODED.RESTAURANT_ID);

        final String bodyJSON = new Gson().toJson(bodyFields);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                HARDCODED.URL + "item/recommend",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
        bodyFields.put("ordered_at", Calendar.getInstance().getTime().toString());
        bodyFields.put("has_paid", "0");
        bodyFields.put("is_active_session", "1");

        final String bodyJSON = new Gson().toJson(bodyFields);
        // TODO: MAKE POST /order
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                HARDCODED.URL + "/order",
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
    }

    private void getOrderId() {

    }

    // Get the list of all items in the menu
    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItems(ArrayList<MenuItem> menuItems) {
        for(MenuItem item : menuItems){
            item.quantity = "0";
            this.menuItems.add(item);
            orderedItems.put(item, 0);
        }
    }

    //remove all items from cart
    public void checkout() {

        // TODO: NOTIFY SERVER THAT CUSTOMER ORDER - /order POST once
        // Post /ordered-item/order:id forever
        // ITEMS IN THE ORDERCART BASED ON ITS MENUITEM.QUANTITY
        // Set is active to 2


        for (MenuItem menuItem : menuItems) {
            // Add those value into orderedItems
            orderedItems.replace(
                    menuItem, orderedItems.get(menuItem) + Integer.valueOf(menuItem.quantity)
            );

            // Clear the cart of all orders
            menuItem.quantity = String.valueOf(0);
        }
    }

    // Return a map of MenuItems to the quantity ordered in the backend
    public HashMap<MenuItem, Integer> getBill() {
        return orderedItems;
    }
}
