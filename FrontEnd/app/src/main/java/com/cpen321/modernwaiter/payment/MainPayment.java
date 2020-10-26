package com.cpen321.modernwaiter.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cpen321.modernwaiter.R;

public class MainPayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_payment);

        Button pay_for_all = findViewById(R.id.pay_for_all);
        Intent startStripePayment = new Intent(this, StripePayment.class);
        pay_for_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(startStripePayment);
            }
        });

        Button split_evenly = findViewById(R.id.split_evenly);
        split_evenly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });

        Intent intent = new Intent(this, PerItemPayment.class);
        Button pay_per_item = findViewById(R.id.pay_per_item);
        pay_per_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}