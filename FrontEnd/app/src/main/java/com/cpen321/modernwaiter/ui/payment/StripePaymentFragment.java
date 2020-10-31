package com.cpen321.modernwaiter.ui.payment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.application.API;
import com.cpen321.modernwaiter.application.MainActivity;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.application.MenuItem;
import com.cpen321.modernwaiter.ui.bill.BillRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;


public class StripePaymentFragment extends Fragment {
    /**
     * This example collects card payments, implementing the guide here: https://stripe.com/docs/payments/accept-a-payment-synchronously#android
     */
    private int totalAmount = 0;
    private Stripe stripe;
    private View view;
    private final RequestQueue requestQueue = MainActivity.requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stripe, container, false);

        loadBillRecycler();
        loadPage();
        return view;
    }

    private void loadBillRecycler() {
        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stripe_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        HashMap<MenuItem, Integer> billMap = tableSession.getBill();

        BillRecyclerAdapter billRecyclerAdapter = new BillRecyclerAdapter(billMap);
        recyclerView.setAdapter(billRecyclerAdapter);

        totalAmount = 0;

        tableSession.getBill().forEach(((menuItem, count) ->
                totalAmount += menuItem.getCost() * count));

        Button payButton = view.findViewById(R.id.payButton);
        payButton.setText("Pay $" + new DecimalFormat("#.##")
                .format((double) totalAmount / 100));
    }

    private void loadPage() {
        // Clear the card widget
        CardInputWidget cardInputWidget = view.findViewById(R.id.cardInputWidget);
        cardInputWidget.clear();

        requestKey();
    }

    private void requestKey() {
        // For added security, our sample app gets the publishable key from the server

        StringRequest request = new StringRequest(
            Request.Method.GET, API.stripeKey,
            response -> {
                Map<String, String> responseMap = parseResponseToMap(response);

                final String stripePublishableKey = responseMap.get("publishableKey");
                if (stripePublishableKey != null) {
                    System.out.println(stripePublishableKey);
                    onRetrievedKey(stripePublishableKey);
                }
            },

            error -> Log.i("Request Stripe Key", error.toString())
        );

        tableSession.requestQueue.add(request);
    }

    private void pay() {
        CardInputWidget cardInputWidget = view.findViewById(R.id.cardInputWidget);
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

        if (params == null) {
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
                Log.i("STRIPEPAYMENT", "error in pay()");
            }
        });
    }

    private void sendPaymentMethod(@Nullable String paymentMethodId) {

        final Map<String, String> bodyFields = new HashMap<>();

        // TODO: GENERATE THIS AUTOMATICALLY
        if (paymentMethodId != null) {
            bodyFields.put("useStripeSdk", "true");
            bodyFields.put("paymentMethodId", paymentMethodId);
            bodyFields.put("currency", "cad");
            // TODO:
            bodyFields.put("orderAmount", String.valueOf(totalAmount));
        } else {
            bodyFields.put("paymentIntentId", null);
        }

        final String bodyJSON = new Gson().toJson(bodyFields);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, API.stripePay,
                response -> {
                    System.out.println(response);

                    Map<String, String> responseMap = parseResponseToMap(response);

                    String error = responseMap.get("error");
                    String paymentIntentClientSecret = responseMap.get("clientSecret");
                    String requiresAction = responseMap.get("requiresAction");

                    if (error != null) {
                        Log.i("Stripe Payment", "Error, response is " + error);
                    } else if (paymentIntentClientSecret != null) {
                        if ("true".equals(requiresAction)) {
                            stripe.handleNextActionForPayment(requireActivity(), paymentIntentClientSecret);
                        } else {
                            putPaid();
                            endSession();
                            tableSession.isActive = false;
                            Navigation.findNavController(view).navigate(R.id.action_navigation_stripe_to_navigation_post_payment);
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

        requestQueue.add(stringRequest);
    }

    private Map<String, String> parseResponseToMap(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        final Map<String, String> responseMap;
        if (response != null) {
            responseMap = gson.fromJson(response, type);
        } else {
            responseMap = new HashMap<>();
        }

        return responseMap;
    }

    private void onRetrievedKey(@NonNull String stripePublishableKey) {
        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        PaymentConfiguration.init(requireActivity(), stripePublishableKey);
        stripe = new Stripe(requireActivity(), stripePublishableKey);

        // Hook up the pay button to the card widget and stripe instance
        Button payButton = view.findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> pay());
    }

    /**
     * PUT request to notify backend that the amount has been paid
     * RIGHT NOW FOR PAY_FOR_ALL ONLY
     */
    private void putPaid(){
        //PUT request to confirm that the order has been paid
        String url = API.paidOrderItems;
        //TODO: pass order_id, needs billfragment
        HashMap<MenuItem, Integer> orderedItems = tableSession.getBill();
        for( Map.Entry<MenuItem, Integer> item : orderedItems.entrySet() ) {
            Map<String,String> params = new HashMap<>();
            params.put("orderId", String.valueOf(tableSession.orderId));
            params.put("itemId", String.valueOf(item.getKey().id));
            params.put("hasPaid", "1");
            JSONObject parameters = new JSONObject(params);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT, url, parameters,
                    response -> {
                        //on success
                        //TODO: print some message or not
                    }, error -> Log.i("Request put paid item", error.toString())
            );

            MainActivity.requestQueue.add(jsonObjectRequest);
        }
        //PUT request for order has been paid fully
        String url_order_paid = API.paidOrder;
        Map<String,String> params = new HashMap<>();
        params.put("orderId", String.valueOf(tableSession.orderId));
        params.put("hasPaid", "1");
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url_order_paid, parameters,
                response -> {
                //on success
                //TODO: print some message or not
            }, error -> Log.i("Request put paid full", error.toString())
        );

        MainActivity.requestQueue.add(jsonObjectRequest);

    }

    private void endSession(){
        // PUT request for order has been paid fully
        String url = API.orderSession;
        final Map<String,String> params = new HashMap<>();
        params.put("orderId", String.valueOf(tableSession.orderId));
        params.put("isActive", "0");
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, parameters,
                response -> {
                    //on success
                    //TODO: print some message or not
                }, error -> Log.i("Request end session", error.toString())
        );

        MainActivity.requestQueue.add(jsonObjectRequest);
    }
}
