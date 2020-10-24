package com.cpen321.modernwaiter.ui.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cpen321.modernwaiter.Bill;
import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.payment.MainPayment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BillFragment extends Fragment{
    private String url = "https://192.168.56.1:8080/time";
    private String server = "https://localhost/8080";
    private RequestQueue queue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_bill, container, false);

        // TODO: SHOW THE BILL
        Bill bill = getBill();


        Button startPaymentButton = root.findViewById(R.id.startPaymentButton);
        startPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainPayment.class);
                startActivity(intent);
            }
        });

        return root;
    }

    //function to make REST GET request
    private Bill getBill(){

        Bill bill = new Bill();
        //TODO : add right GET calls
        int id = MainActivity.getID();

        String url = "http://my-json-feed";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("data");
                            for( int i = 0; i<jsonArray.length (); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String item_name = jsonObject.getString("name");
                                double item_price = jsonObject.getDouble("cost");
                                bill.Bill_add_item(i, item_name,item_price);
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
        return bill;
    }
}