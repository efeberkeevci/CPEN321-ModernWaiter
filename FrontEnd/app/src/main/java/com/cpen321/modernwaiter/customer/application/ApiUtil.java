package com.cpen321.modernwaiter.customer.application;

//import com.stripe.android.model.PaymentMethodCreateParams;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ApiUtil {
    public static final String RESTAURANT_ID = "1";
    public static String TABLE_ID = "1";
    public static String USER_ID = "1";

    public static final String URL = "http://52.170.16.110:3000/";

    public static final String usersGoogle = URL + "users/google/";
    public static final String users = URL + "users/";

    //preferences
    public static final String choices = URL + "users/preferences";
    public static final String[] choices_name_list = {"Mild-spice-level", "Medium-spice-level",
            "Hot-spice-level", "lactose-intolerant", "vegan-only", "vegetarian-only",
            "non-alcoholic-drinks-only", "no-sugar-added-drinks", "gluten-free"};
    public static final String items = URL + "items/";

    public static final String health = URL + "_health";
    public static final String userOrder = URL + "orders/user/";
    public static final String orderedItems = URL + "ordered-items/";
    public static final String orderSelect = URL + "ordered-items/selected/";

    public static final String isActive = "?isActive=1";
    public static final String recommend = URL + "recommendation/";
    public static final String order = URL + "orders/";

    public static final String paidOrderItems = URL + "ordered-items/paid/";
    public static final String paidOrder = URL + "orders/paid/";
    public static final String orderSession = URL + "orders/session/";

    public static final String orderTable = URL + "orders/table/";

    public static final String stripeKey = URL + "key/";
    public static final String stripePay = URL + "pay/";

    // Notification
    public static final String registration = URL + "registrationToken/";
    public static final String unsubscribe = URL + "unsubscribedToken/";
}
