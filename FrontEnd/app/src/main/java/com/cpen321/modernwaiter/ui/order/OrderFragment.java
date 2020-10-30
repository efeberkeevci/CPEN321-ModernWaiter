package com.cpen321.modernwaiter.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;

import java.util.HashMap;

import static com.cpen321.modernwaiter.MainActivity.tableSession;

public class OrderFragment extends Fragment {

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

        RecyclerView recyclerView = view.findViewById(R.id.order_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        HashMap<MenuItem, Integer> billMap = MainActivity.tableSession.getCart();

        OrderRecyclerAdapter orderRecyclerAdapter = new OrderRecyclerAdapter(billMap);
        recyclerView.setAdapter(orderRecyclerAdapter);

        Button checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableSession.checkout();
                while (orderRecyclerAdapter.itemArray.size() != 0) {
                    orderRecyclerAdapter.itemArray.remove(0);
                    orderRecyclerAdapter.notifyItemRemoved(0);
                    orderRecyclerAdapter.notifyItemRangeChanged(0, orderRecyclerAdapter.itemArray.size());
                }
            }
        });

        return view;
    }
}