package com.cpen321.modernwaiter;

import com.cpen321.modernwaiter.ui.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
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

    //creates a new session
    TableSession() {

        // TODO: Initialize the menu
        //Make request to server to retrieve menu items to display

        menuItems = MainActivity.menu_items;

        orderedItems = menuItems.stream().collect(
                Collectors.toMap(x -> x, x -> 0, (s, a) -> s, HashMap::new)
        );

    }

    // Get the list of all items in the menu
    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    //remove all items from cart
    public void checkout() {

        // TODO: NOTIFY SERVER THAT CUSTOMER ORDER
        // ITEMS IN THE ORDERCART BASED ON ITS MENUITEM.QUANTITY


        for (MenuItem menuItem : menuItems) {
            // Add those value into orderedItems
            orderedItems.replace(
                    menuItem, orderedItems.get(menuItem) + Integer.valueOf(menuItem.quantity)
            );

            // Clear the cart of all orders
            menuItem.quantity = String.valueOf(0);
        }
    }

    // TODO: Update the bill based on the backend
    public void UpdateBill() {
        int id = MainActivity.getID();

        /*String url = "http://my-json-feed";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("data");
                            for( int i = 0; i<jsonArray.length (); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String item_name = jsonObject.getString("name");
                                double item_price = jsonObject.getDouble("cost");
                                String item_id = jsonObject.getString("id");
                                //TODO: check logic for these values, I ignore description and quantity when getting data from backend
                                orderCart.add(new MenuItem(item_name,"", "1", item_id, item_price));
                            }
                            updateOrderQuantities();
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        MainActivity.requestQueue.add(jsonObjectRequest);
        */
    }

    // Return a map of MenuItems to the quantity ordered in the backend
    public HashMap<MenuItem, Integer> getBill() {
        return orderedItems;
    }
}
