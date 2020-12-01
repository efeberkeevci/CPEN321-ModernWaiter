package com.cpen321.modernwaiter.customer.ui.payment;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cpen321.modernwaiter.R;

public class MainPaymentFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        Button pay_for_all = view.findViewById(R.id.barcode_button);
        pay_for_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("mode", "pay_all");
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_navigation_stripe, bundle);
            }
        });

        Button split_evenly = view.findViewById(R.id.option_button);
        split_evenly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("mode", "split_even");
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_navigation_stripe, bundle);
            }
        });

        Button pay_per_item = view.findViewById(R.id.demo_button);
        pay_per_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_per_item_payment);
            }
        });
        return view;
    }
}