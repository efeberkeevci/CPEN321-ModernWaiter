package com.cpen321.modernwaiter.ui.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.modernwaiter.R;

public class PayFragment extends Fragment {

    private Button pay_for_all;
    private Button split_evenly;
    private Button pay_per_item;
    final static String TAG = "PayFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_pay, container, false);
        pay_for_all = root.findViewById(R.id.pay_for_all);
        pay_for_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to pay for all");
                Toast toast = Toast.makeText(getActivity(),"Trying to pay for everyone", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 190);
                toast.show();
            }
        });
        split_evenly = root.findViewById(R.id.split_evenly);
        split_evenly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to split evenly");
                Toast toast = Toast.makeText(getActivity(),"Trying to split evenly and pay", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 190);
                toast.show();
            }
        });
        pay_per_item = root.findViewById(R.id.pay_per_item);
        pay_per_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to pay per item");
                Toast toast = Toast.makeText(getActivity(),"Trying to pay per item", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 190);
                toast.show();
            }
        });

        return root;
    }
}