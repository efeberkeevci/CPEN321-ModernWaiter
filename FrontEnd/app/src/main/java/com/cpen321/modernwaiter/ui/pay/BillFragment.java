package com.cpen321.modernwaiter.ui.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.payment.MainPayment;
import com.cpen321.modernwaiter.ui.MenuItem;

import java.util.HashMap;

public class BillFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BillFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();

        HashMap<MenuItem, Integer> billMap = mainActivity.tableSession.getBill();

        Button startPaymentButton = view.findViewById(R.id.startPaymentButton);
        startPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainPayment.class);
                startActivity(intent);
            }
        });

        // Set the adapter
        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bill_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        BillRecyclerAdapter billRecyclerAdapter = new BillRecyclerAdapter(billMap);
        recyclerView.setAdapter(billRecyclerAdapter);

        return view;
    }

    /**function to make REST GET request
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


        //for now add some dummy values
        //TODO: remove dummy values from here
        bill.Bill_add_item(0,"CheeseSandwich", 10.0);
        bill.Bill_add_item(10, "burger", 15.0);
        return bill;
    }**/
}