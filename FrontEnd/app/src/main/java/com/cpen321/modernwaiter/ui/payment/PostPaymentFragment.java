package com.cpen321.modernwaiter.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.cpen321.modernwaiter.R;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;

public class PostPaymentFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_payment, container, false);

        Button pay_per_item = view.findViewById(R.id.pay_again_button);
        Button go_to_menu = view.findViewById(R.id.go_to_menu_button);
        if (!tableSession.isActive())
            pay_per_item.setVisibility(View.INVISIBLE);
        else go_to_menu.setVisibility(View.INVISIBLE);

        go_to_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_post_payment_to_navigation_menu);
            }
        });

        pay_per_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_post_payment_to_navigation_payment);
            }
        });
        return view;
    }
}