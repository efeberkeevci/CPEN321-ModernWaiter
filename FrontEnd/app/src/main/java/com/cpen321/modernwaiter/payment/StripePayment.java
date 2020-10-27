package com.cpen321.modernwaiter.payment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cpen321.modernwaiter.HARDCODED;
import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class StripePayment extends AppCompatActivity {
    /**
     * This example collects card payments, implementing the guide here: https://stripe.com/docs/payments/accept-a-payment-synchronously#android
     */
    // 10.0.2.2 is the Android emulator's alias to localhost
    private static final String BACKEND_URL = "http://10.0.2.2:3000/";
    private double totalAmount = 0;
    private Stripe stripe;
    private Activity context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        loadPage();
    }

    private void loadPage() {
        // Clear the card widget
        CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
        cardInputWidget.clear();

        requestKey();
    }

    private void requestKey() {
        RequestQueue queue = Volley.newRequestQueue(this);
        // For added security, our sample app gets the publishable key from the server

        StringRequest request = new StringRequest(
                Request.Method.GET,
                BACKEND_URL + "stripe/key",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String, String> responseMap = parseResponseToMap(response);

                        final String stripePublishableKey = responseMap.get("publishableKey");
                        if (stripePublishableKey != null) {
                            System.out.println(stripePublishableKey);
                            onRetrievedKey(stripePublishableKey);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

        queue.add(request);
    }

    private void pay() {
        CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

        if (params == null) {
            return;
        }
        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
                sendPaymentMethod(result.id, null);
            }

            @Override
            public void onError(@NonNull Exception e) {

            }
        });
    }

    private double getAmountToPay(){
        if(MainPayment.option.equals("payForAll")){
            String url = HARDCODED.URL + "order/table/"+ HARDCODED.TABLE_ID + "?isActive=1";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = response.getJSONArray("data");
                                for( int i = 0; i<jsonArray.length (); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    int tableId = jsonObject.getInt("tables_id");
                                    double amount = jsonObject.getDouble("users_id");
                                    int has_paid = jsonObject.getInt("has_paid");
                                    int is_active_session = jsonObject.getInt("is_active_session");
                                    //TODO: check logic for these values, I ignore description and quantity when getting data from backend
                                    totalAmount = totalAmount + amount;
                                }
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
            //Make GET Request to get the bill for the entire table
            return totalAmount;
        }
        return 0;
    }

    private void sendPaymentMethod(@Nullable String paymentMethodId, @Nullable String paymentIntentId) {

        final Map<String, String> bodyFields = new HashMap<>();

        // TODO: GENERATE THIS AUTOMATICALLY
        if (paymentMethodId != null) {
            bodyFields.put("useStripeSdk", "true");
            bodyFields.put("paymentMethodId", paymentMethodId);
            bodyFields.put("currency", "cad");
            // TODO:
            bodyFields.put("amounts", String .valueOf(totalAmount));
            bodyFields.put("items", "fried_rice");
        } else {
            bodyFields.put("paymentIntentId", paymentIntentId);
        }

        final String bodyJSON = new Gson().toJson(bodyFields);
        RequestQueue queue = Volley.newRequestQueue(this);

        Intent startPostPayment = new Intent(this, PostPayment.class);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                BACKEND_URL + "stripe/pay",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        Map<String, String> responseMap = parseResponseToMap(response);

                        String error = responseMap.get("error");
                        String paymentIntentClientSecret = responseMap.get("clientSecret");
                        String requiresAction = responseMap.get("requiresAction");

                        if (error != null) {
                            displayAlert("Error", error, false);
                        } else if (paymentIntentClientSecret != null) {
                            if ("true".equals(requiresAction)) {
                                stripe.handleNextActionForPayment(context, paymentIntentClientSecret);
                            } else {
                                startActivity(startPostPayment);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
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

        queue.add(stringRequest);
    }

    private Map<String, String> parseResponseToMap(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        final Map<String, String> responseMap;
        if (response != null) {
            responseMap = gson.fromJson(response, type);
        } else {
            responseMap = new HashMap<>();
        }

        return responseMap;
    }

    private void displayAlert(@NonNull String title, @NonNull String message, boolean restartDemo) {
        runOnUiThread(() -> {
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(this)
                            .setTitle(title)
                            .setMessage(message);
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            if (restartDemo) {
                builder.setPositiveButton("Restart demo",
                        (DialogInterface dialog, int index) -> loadPage());
            } else {
                builder.setPositiveButton("Ok", null);
            }
            builder
                    .create()
                    .show();
        });
    }

    private void onRetrievedKey(@NonNull String stripePublishableKey) {
        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        final Context applicationContext = getApplicationContext();
        PaymentConfiguration.init(applicationContext, stripePublishableKey);
        stripe = new Stripe(applicationContext, stripePublishableKey);

        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> pay());
    }
}
