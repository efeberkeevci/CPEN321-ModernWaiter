package com.cpen321.modernwaiter.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;

public class PostPayment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_payment, container, false);

        Button pay_per_item = view.findViewById(R.id.pay_again_button);
        if (!((MainActivity) getActivity()).tableSession.isActive)
            pay_per_item.setVisibility(View.INVISIBLE);

        pay_per_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_post_payment_to_navigation_payment);
            }
        });
        return view;
    }
}