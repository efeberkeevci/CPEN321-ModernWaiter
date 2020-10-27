package com.cpen321.modernwaiter.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.payment.MainPayment;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.cpen321.modernwaiter.ui.menu.DetailItemFragment;
import com.cpen321.modernwaiter.ui.pay.BillFragment;

import java.util.HashMap;

public class OrderFragment extends Fragment {

    public final Fragment thisFragment =this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        Button startBillButton = view.findViewById(R.id.startBillButton);
        startBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_order_to_navigation_bill);
            }
        });


        // Set the adapter
        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.order_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MainActivity mainActivity = (MainActivity) getActivity();
        HashMap<MenuItem, Integer> billMap = mainActivity.tableSession.getCart();

        OrderRecyclerAdapter orderRecyclerAdapter = new OrderRecyclerAdapter(billMap);
        recyclerView.setAdapter(orderRecyclerAdapter);

        Button checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.tableSession.checkout();
                orderRecyclerAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}