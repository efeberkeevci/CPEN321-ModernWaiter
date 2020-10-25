package com.cpen321.modernwaiter;

import com.cpen321.modernwaiter.ui.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*  Contains all the data required for this table's session such as its
    cart & the restaurant's menu.
 */
public class TableSession {

    private HashMap<Integer, Integer> orderedItems;
    private final ArrayList<MenuItem> orderCart;

    TableSession(List<MenuItem> menu) {

        orderCart = new ArrayList<>(menu);
        orderedItems = orderCart.stream().collect(
                Collectors.toMap(x -> x.id, x -> 0, (s, a) -> s, HashMap::new)
        );

    }

    public ArrayList<MenuItem> getMenu() {
        return orderCart;
    }

    public void checkout() {

        // TODO: NOTIFY SERVER THAT CUSTOMER ORDER
        // ITEMS IN THE ORDERCART BASED ON ITS MENUITEM.QUANTITY


        for (MenuItem menuItem : orderCart) {
            // Add those value into orderedItems
            orderedItems.replace(
                    menuItem.id, orderedItems.get(menuItem.id) + 1
            );

            // Clear the cart of all orders
            menuItem.quantity = String.valueOf(0);
        }
    }

    public void stop() {
        System.out.println(this);
    }

    // TODO: ADD me a method that can return the bill

}
