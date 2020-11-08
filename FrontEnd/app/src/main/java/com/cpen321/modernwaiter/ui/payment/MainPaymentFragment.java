package com.cpen321.modernwaiter.ui.payment;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cpen321.modernwaiter.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPaymentFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.INVISIBLE);

        Button pay_for_all = view.findViewById(R.id.pay_for_all);
        pay_for_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_navigation_stripe);
            }
        });

        Button split_evenly = view.findViewById(R.id.split_evenly);
        split_evenly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_navigation_stripe);
            }
        });

        Button pay_per_item = view.findViewById(R.id.pay_per_item);
        pay_per_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_payment_to_per_item_payment);
            }
        });
        return view;
    }
}