package com.cpen321.modernwaiter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cpen321.modernwaiter.ui.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*  Contains all the data required for this table's session such as its
    cart & the restaurant's menu.
 */
public class TableSession {

    private HashMap<MenuItem, Integer> orderedItems;
    private final ArrayList<MenuItem> orderCart;

    //creates a new session
    TableSession() {
        orderCart = new ArrayList<>();
        orderedItems = orderCart.stream().collect(
                Collectors.toMap(x -> x, x -> 0, (s, a) -> s, HashMap::new)
        );

    }

    //get the list of all ordered items
    public ArrayList<MenuItem> getOrderCart() {
        return orderCart;
    }

    //remove all items from cart
    public void checkout() {

        // TODO: NOTIFY SERVER THAT CUSTOMER ORDER
        // ITEMS IN THE ORDERCART BASED ON ITS MENUITEM.QUANTITY


        for (MenuItem menuItem : orderCart) {
            // Add those value into orderedItems
            orderedItems.replace(
                    menuItem, orderedItems.get(menuItem) + Integer.valueOf(menuItem.quantity)
            );

            // Clear the cart of all orders
            menuItem.quantity = String.valueOf(0);
        }
    }

    public void stop() {
        System.out.println(this);
    }

    //get the bill from backend
    public HashMap<MenuItem, Integer> getBill() {

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
        //TODO: delete these dummy values
        orderCart.add(new MenuItem("CheeseBurger", "BLAH BLAH BLAH", "1","1" ,10.50));
        orderCart.add(new MenuItem("BigMac", "DIBIDI DABIDI BOO", "1","2" ,12.50));
        orderCart.add(new MenuItem("CheeseBurger", "BLAH BLOO BLAH BLOO", "1","1" ,10.50));
        updateOrderQuantities();
        return orderedItems;
    }

    //update the orderedItems hashmap to reflect the quantity ordered for one item_id
    private void updateOrderQuantities(){
        for (MenuItem menuItem : orderCart) {
            // Add those value into orderedItems
            orderedItems.replace(
                    menuItem, orderedItems.get(menuItem) + Integer.valueOf(menuItem.quantity)
            );
        }
    }

}
