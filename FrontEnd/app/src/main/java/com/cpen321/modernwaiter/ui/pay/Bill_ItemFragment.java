package com.cpen321.modernwaiter.ui.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Bill_ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Bill_ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Bill_ItemFragment newInstance(int columnCount) {
        Bill_ItemFragment fragment = new Bill_ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        Button startPaymentButton = view.findViewById(R.id.startPaymentButton);
        startPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainPayment.class);
                startActivity(intent);
            }
        });

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new Bill_ItemRecyclerViewAdapter(getBill().bill_list));
        }
        return view;
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


        //for now add some dummy values
        //TODO: remove dummy values from here
        bill.Bill_add_item(0,"CheeseSandwich", 10.0);
        bill.Bill_add_item(10, "burger", 15.0);
        return bill;
    }
}