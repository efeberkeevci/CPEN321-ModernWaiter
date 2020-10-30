package com.cpen321.modernwaiter.payment;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.HARDCODED;
import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.cpen321.modernwaiter.ui.pay.BillRecyclerAdapter;
import com.google.gson.Gson;
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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StripePayment extends Fragment {
    /**
     * This example collects card payments, implementing the guide here: https://stripe.com/docs/payments/accept-a-payment-synchronously#android
     */
    private int totalAmount = 0;
    private Stripe stripe;
    private View view;
    private int num_users = 1;
    private final RequestQueue requestQueue = MainActivity.requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stripe, container, false);

        String option_text;
        String amount_text;

        //getAmountToPay();
        if(MainPayment.option.equals("payForAll")) option_text = "Total amount to be paid is:";
        else if(MainPayment.option.equals("paySplitEvenly")) option_text = "Total amount to be paid by you after splitting evenly is:";
        else if(MainPayment.option.equals("payPerItem")) option_text = "Toatl amount to be paid by you for the items you selected is:";
        else option_text = "Oops! Looks like something went wrong with your billing";

        loadBillRecycler();
        loadPage();
        return view;
    }

    private void loadBillRecycler() {
        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stripe_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MainActivity mainActivity = (MainActivity) getActivity();

        HashMap<MenuItem, Integer> billMap = mainActivity.tableSession.getBill();

        BillRecyclerAdapter billRecyclerAdapter = new BillRecyclerAdapter(billMap);
        recyclerView.setAdapter(billRecyclerAdapter);

        totalAmount = 0;

        for(MenuItem menuItem : mainActivity.tableSession.getBill().keySet()) {
            totalAmount += menuItem.getCost() * mainActivity.tableSession.getBill().get(menuItem);
        }

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
        RequestQueue queue = requestQueue;
        // For added security, our sample app gets the publishable key from the server

        StringRequest request = new StringRequest(
            Request.Method.GET, HARDCODED.URL + "key",
            response -> {
                Map<String, String> responseMap = parseResponseToMap(response);

                final String stripePublishableKey = responseMap.get("publishableKey");
                if (stripePublishableKey != null) {
                    System.out.println(stripePublishableKey);
                    onRetrievedKey(stripePublishableKey);
                }
            },

            error -> System.out.println(error));

        queue.add(request);
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
                sendPaymentMethod(result.id, null);
            }

            @Override
            public void onError(@NonNull Exception e) {
                Log.i("STRIPEPAYMENT", "error in pay()");
            }
        });
    }

    /**
     * Function to get the amount to be paid by a user
     * @return totalAmount
     */
    private double getAmountToPay(){
        Log.i("STRIPEPAYMENT", "INSIDE GETaMOUNTtOPAY");
        if(MainPayment.option.equals("payPerItem")){
            totalAmount = 0;
            String url = HARDCODED.URL + "order/user/" + HARDCODED.USER_ID + "?isActive=1";
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        //only need this user's bill
                        JSONObject jsonObject = response.getJSONObject(0);
                        int id = jsonObject.getInt("id");
                        int tableId = jsonObject.getInt("tables_id");
                       // double amount = jsonObject.getDouble("users_id");
                        int has_paid = jsonObject.getInt("has_paid");
                        int is_active_session = jsonObject.getInt("is_active_session");
                        //TODO: check logic for these values, I ignore description and quantity when getting data from backend
                        totalAmount = 1600;

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }, error -> {
                    // TODO: Handle error
            });

            MainActivity.requestQueue.add(jsonObjectRequest);
            //Make GET Request to get the bill for the entire table
            return totalAmount;
        }
        else{
            Log.i("STRIPEPAYMENT", "INSIDE else");
            totalAmount = 0;
            String url = HARDCODED.URL + "order/table/"+ HARDCODED.TABLE_ID + "?isActive=1";
            StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<OrderResponse> orderResponseList = new Gson()
                            .fromJson(response, new TypeToken<List<OrderResponse>>() {}.getType());

                    totalAmount = (int) (orderResponseList.get(0).amount * 100);
                },
                error -> Log.i("asd", error.toString())
            );
            MainActivity.requestQueue.add(request);
            //get the bill for the entire table
            if(MainPayment.option.equals("payForAll")){
                return totalAmount;
            }
            //get bill after splitting evenly
            else if(MainPayment.option.equals("paySplitEvenly")){
                return totalAmount/num_users;
            }
            //it should never get here really !!
            else {
                totalAmount = 0;
                return totalAmount;
            }
        }
    }

    private void sendPaymentMethod(@Nullable String paymentMethodId, @Nullable String paymentIntentId) {

        final Map<String, String> bodyFields = new HashMap<>();

        // TODO: GENERATE THIS AUTOMATICALLY
        if (paymentMethodId != null) {
            bodyFields.put("useStripeSdk", "true");
            bodyFields.put("paymentMethodId", paymentMethodId);
            bodyFields.put("currency", "cad");
            // TODO:
            bodyFields.put("orderAmount", String.valueOf(totalAmount));
        } else {
            bodyFields.put("paymentIntentId", paymentIntentId);
        }

        final String bodyJSON = new Gson().toJson(bodyFields);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, HARDCODED.URL + "pay",
                response -> {
                    System.out.println(response);

                    Map<String, String> responseMap = parseResponseToMap(response);

                    String error = responseMap.get("error");
                    String paymentIntentClientSecret = responseMap.get("clientSecret");
                    String requiresAction = responseMap.get("requiresAction");

                    if (error != null) {
                        Log.i("Stripe Payment", "Error, response is  not null");
                    } else if (paymentIntentClientSecret != null) {
                        if ("true".equals(requiresAction)) {
                            stripe.handleNextActionForPayment(getActivity(), paymentIntentClientSecret);
                        } else {
                            putPaid();
                            endSession();
                            MainActivity.tableSession.isActive = false;
                            Navigation.findNavController(view).navigate(R.id.action_navigation_stripe_to_navigation_post_payment);
                        }
                    }
                },
                error -> System.out.println(error)
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

    private void onRetrievedKey(@NonNull String stripePublishableKey) {
        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        PaymentConfiguration.init(getActivity(), stripePublishableKey);
        stripe = new Stripe(getActivity(), stripePublishableKey);

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
        String url = HARDCODED.URL + "ordered-items/paid/";
        //TODO: pass order_id, needs billfragment
        HashMap<MenuItem, Integer> orderedItems = MainActivity.tableSession.getBill();
        for( Map.Entry<MenuItem, Integer> item : orderedItems.entrySet() ) {
            Map<String,String> params = new HashMap<>();
            params.put("orderId", String.valueOf(MainActivity.tableSession.orderId));
            params.put("itemId", String.valueOf(item.getKey().id));
            params.put("hasPaid", "1");
            JSONObject parameters = new JSONObject(params);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT, url, parameters,
                    response -> {
                        //on success
                        //TODO: print some message or not
                    }, error -> {
                        error.printStackTrace();
                        // TODO: Handle error

                    });

            MainActivity.requestQueue.add(jsonObjectRequest);
        }
        //PUT request for order has been paid fully
        String url_order_paid = HARDCODED.URL + "order/paid/";
        Map<String,String> params = new HashMap<>();
        params.put("orderId", String.valueOf(MainActivity.tableSession.orderId));
        params.put("hasPaid", "1");
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url_order_paid, parameters,
                response -> {
                //on success
                //TODO: print some message or not
                }, error -> {
                    error.printStackTrace();
                    // TODO: Handle error
                });

        MainActivity.requestQueue.add(jsonObjectRequest);

    }

    private void endSession(){
        //PUT request for order has been paid fully
        String url = HARDCODED.URL + "order/session/";
        Map<String,String> params = new HashMap<>();
        params.put("orderId", String.valueOf(MainActivity.tableSession.orderId));
        params.put("isActive", "0");
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, parameters,
                response -> {
                    //on success
                    //TODO: print some message or not
                }, error -> {
                    error.printStackTrace();
                    // TODO: Handle error

                });

        MainActivity.requestQueue.add(jsonObjectRequest);
    }

    public class OrderResponse {
        public double amount;
    }
}
