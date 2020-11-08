package com.cpen321.modernwaiter.ui.payment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.application.ApiUtil;
import com.cpen321.modernwaiter.application.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;

public class StripePaymentController {

    private Stripe stripe;
    private FragmentActivity context;
    private int totalAmount;

    StripePaymentController(FragmentActivity context, int totalAmount) {
        this.context = context;
        this.totalAmount = totalAmount;
        requestKey();
    }

    private void requestKey() {
        // For added security, our sample app gets the publishable key from the server
        StringRequest request = new StringRequest(
                Request.Method.GET, ApiUtil.stripeKey,
                response -> {
                    final Map<String, String> responseMap;
                    if (response != null) {
                        responseMap = new Gson().fromJson(response,
                                new TypeToken<Map<String, String>>() {}.getType());
                    } else {
                        responseMap = new HashMap<>();
                    }

                    final String stripePublishableKey = responseMap.get("publishableKey");
                    if (stripePublishableKey != null) {
                        onRetrievedKey(stripePublishableKey);
                    }
                },

                error -> Log.i("Request Stripe Key", error.toString())
        );

        tableSession.add(request);
    }

    private void onRetrievedKey(@NonNull String stripePublishableKey) {
        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        PaymentConfiguration.init(context, stripePublishableKey);
        stripe = new Stripe(context, stripePublishableKey);
    }

    public void pay(PaymentMethodCreateParams params) {
        if (stripe == null || params == null) {
            return;
        }

        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
                sendPaymentMethod(result.id);
            }

            @Override
            public void onError(@NonNull Exception e) {
                Log.i("Stripe API Call", "error in pay()");
            }
        });
    }

    private void sendPaymentMethod(@Nullable String paymentMethodId) {

        final Map<String, String> bodyFields = new HashMap<>();

        if (paymentMethodId != null) {
            bodyFields.put("useStripeSdk", "true");
            bodyFields.put("paymentMethodId", paymentMethodId);
            bodyFields.put("currency", "cad");
            bodyFields.put("orderAmount", String.valueOf(totalAmount));
        } else {
            bodyFields.put("paymentIntentId", null);
        }

        final String bodyJSON = new Gson().toJson(bodyFields);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUtil.stripePay,
                response -> {
                    final Map<String, String> responseMap;
                    if (response != null) {
                        responseMap = new Gson().fromJson(response,
                                new TypeToken<Map<String, String>>() {}.getType());
                    } else {
                        responseMap = new HashMap<>();
                    }

                    String error = responseMap.get("error");
                    String paymentIntentClientSecret = responseMap.get("clientSecret");
                    String requiresAction = responseMap.get("requiresAction");

                    if (error != null) {
                        Log.i("Stripe Payment", "Error, response is " + error);
                    } else if (paymentIntentClientSecret != null) {
                        if ("true".equals(requiresAction)) {
                            stripe.handleNextActionForPayment(context, paymentIntentClientSecret);
                        } else {
                            putPaid();
                            endSession();
                            tableSession.endSession();
                        }
                    }
                },
                error -> Log.i("Request Stripe Key", error.toString())
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

        tableSession.add(stringRequest);
    }

    /**
     * PUT request to notify backend that the amount has been paid
     * RIGHT NOW FOR PAY_FOR_ALL ONLY
     */
    private void putPaid() {
        //PUT request to confirm that the order has been paid
        String url = ApiUtil.paidOrderItems;

        HashMap<MenuItem, Integer> orderedItems = tableSession.getBill();
        for( Map.Entry<MenuItem, Integer> item : orderedItems.entrySet() ) {
            Map<String,String> params = new HashMap<>();
            params.put("orderId", String.valueOf(tableSession.getOrderId()));
            params.put("itemId", String.valueOf(item.getKey().id));
            params.put("hasPaid", "1");
            JSONObject parameters = new JSONObject(params);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT, url, parameters,
                    response -> {

                    }, error -> Log.i("Request put paid item", error.toString())
            );

            tableSession.add(jsonObjectRequest);
        }
        //PUT request for order has been paid fully
        String url_order_paid = ApiUtil.paidOrder;
        Map<String,String> params = new HashMap<>();
        params.put("orderId", String.valueOf(tableSession.getOrderId()));
        params.put("hasPaid", "1");
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url_order_paid, parameters,
                response -> {
                    //on success
                }, error -> Log.i("Request put paid full", error.toString())
        );

        tableSession.add(jsonObjectRequest);

    }

    private void endSession(){
        // PUT request for order has been paid fully
        String url = ApiUtil.orderSession;
        final Map<String,String> params = new HashMap<>();
        params.put("orderId", String.valueOf(tableSession.getOrderId()));
        params.put("isActive", "0");
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, parameters,
                response -> {
                    //on success
                }, error -> Log.i("Request end session", error.toString())
        );

        tableSession.add(jsonObjectRequest);
    }
}
