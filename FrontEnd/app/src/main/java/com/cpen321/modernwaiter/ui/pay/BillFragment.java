package com.cpen321.modernwaiter.ui.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.payment.MainPayment;

public class BillFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_bill, container, false);

        // TODO: SHOW THE BILL

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
}