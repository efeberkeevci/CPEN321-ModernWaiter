package com.cpen321.modernwaiter.application;

import com.android.volley.Request;
import com.cpen321.modernwaiter.ui.payment.peritem.PaymentItem;

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
    ArrayList<PaymentItem> getOrderList();
    int getUserCount();

    void updateItemSelected(PaymentItem orderItem);
    void checkout();

    void fetchBill();
    void fetchOrderList();

    void add(Request request);

    void endSession();
}
