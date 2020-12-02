package com.cpen321.modernwaiter.customer.application;

import com.android.volley.Request;
import com.cpen321.modernwaiter.customer.ui.payment.peritem.PaymentItem;
import com.google.android.material.chip.Chip;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface SessionInterface {

    boolean isActive();
    int getOrderId();

    Set<MenuItem> getMenuItems();
    HashMap<MenuItem, Integer> getBill();
    HashMap<MenuItem, Integer> getCart();
    MenuItem getFeatureItem();
    ArrayList<PaymentItem> getOrderList();
    int getUserCount();

    void updateItemSelected(PaymentItem orderItem);
    void checkout();

    void fetchOrderList();

    void add(Request request);

    String getUsernameFromId(int id);

    int getUserId();

    void fetchUserRecommendation();

    HashSet<String> getUserPreference();

    void endSession();
}
