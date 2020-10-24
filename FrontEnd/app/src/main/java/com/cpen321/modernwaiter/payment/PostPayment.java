package com.cpen321.modernwaiter.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cpen321.modernwaiter.R;

public class PostPayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_payment);

        Button pay_per_item = findViewById(R.id.pay_again_button);
        Intent startMainPayment = new Intent(this, MainPayment.class);
        pay_per_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(startMainPayment);
            }
        });
    }
}