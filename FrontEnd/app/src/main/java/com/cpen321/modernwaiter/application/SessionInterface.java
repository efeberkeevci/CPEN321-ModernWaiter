package com.cpen321.modernwaiter.application;

import com.android.volley.Request;
import com.cpen321.modernwaiter.ui.order.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface SessionInterface {

    boolean isActive();
    int getOrderId();

    Set<MenuItem> getMenuItems();
    HashMap<MenuItem, Integer> getBill();
    HashMap<MenuItem, Integer> getCart();
    MenuItem getFeatureItem();
    ArrayList<OrderItem> getOrderList();

    void updateItemSelected(OrderItem orderItem);
    void checkout();

    void fetchBill();
    void fetchOrderList();

    void add(Request request);

    void endSession();
}
