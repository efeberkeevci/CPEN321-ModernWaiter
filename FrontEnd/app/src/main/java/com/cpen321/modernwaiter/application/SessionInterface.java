package com.cpen321.modernwaiter.application;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Set;

public interface SessionInterface {

    boolean isActive();
    int getOrderId();

    Set<MenuItem> getMenuItems();
    HashMap<MenuItem, Integer> getBill();
    HashMap<MenuItem, Integer> getCart();
    MenuItem getFeatureItem();

    void checkout();
    void updateBill();
    void add(Request request);

    void endSession();
}
