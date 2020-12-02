package com.cpen321.modernwaiter.customer.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.customer.application.MenuItem;

import java.text.DecimalFormat;
import java.util.HashMap;

import static com.cpen321.modernwaiter.customer.application.CustomerActivity.tableSession;

public class BillFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);



        RecyclerView recyclerView = view.findViewById(R.id.bill_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        HashMap<MenuItem, Integer> billMap = tableSession.getBill();
        BillRecyclerAdapter billRecyclerAdapter = new BillRecyclerAdapter(billMap);
        recyclerView.setAdapter(billRecyclerAdapter);

        TextView textView = view.findViewById(R.id.emptyLabel);
        textView.setVisibility(billRecyclerAdapter.isEmpty() ? View.VISIBLE : View.INVISIBLE);

        int amount = tableSession.getOrderList().stream()
                .reduce(0, (subtotal, menuItemBooleanPair) -> subtotal + menuItemBooleanPair.menuItem.getCost(), Integer::sum);

        String amountText = "$" + new DecimalFormat("#.##")
                .format((double) amount / 100);
        TextView totalPrice = view.findViewById(R.id.totalPrice);
        totalPrice.setText(amountText);

        Button pay_for_all = view.findViewById(R.id.payForAll);
        pay_for_all.setVisibility(billRecyclerAdapter.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        pay_for_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("mode", "pay_all");
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_navigation_stripe, bundle);
            }
        });

        Button split_evenly = view.findViewById(R.id.splitEvenly);
        split_evenly.setVisibility(billRecyclerAdapter.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        split_evenly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("mode", "split_even");
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_navigation_stripe, bundle);
            }
        });

        Button pay_per_item = view.findViewById(R.id.payPerItem);
        pay_per_item.setVisibility(billRecyclerAdapter.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        pay_per_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_per_item_payment);
            }
        });
        return view;
    }

}