package com.cpen321.modernwaiter.application;

public class API {
    public static final String RESTAURANT_ID = "1";
    public static final String TABLE_ID = "1";
    public static final String USER_ID = "1";

    public static final String URL = "http://52.188.158.129:3000/";

    public static final String items = URL + "items/";
    public static final String userOrder = URL + "order/user/";
    public static final String orderedItems = URL + "ordered-items/";
    public static final String isActive = "?isActive=1";
    public static final String recommend = URL + "item/recommend";
    public static final String checkout = URL + "checkout";
    public static final String order = URL + "order";

    public static final String paidOrderItems = URL + "ordered-items/paid/";
    public static final String paidOrder = URL + "order/paid/";
    public static final String orderSession = URL + "order/session/";

    public static final String stripeKey = URL + "key";
    public static final String stripePay = URL + "pay";
}
