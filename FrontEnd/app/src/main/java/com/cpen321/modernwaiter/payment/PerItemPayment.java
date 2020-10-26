package com.cpen321.modernwaiter.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpen321.modernwaiter.DummyContent;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;

import java.util.ArrayList;

public class PerItemPayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_item_payment);

        PerItemRecyclerAdapter.OnItemClickListener listener = new PerItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                // TODO:
            }
        };

        RecyclerView recyclerView = findViewById(R.id.per_item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PerItemRecyclerAdapter perItemRecyclerAdapter = new PerItemRecyclerAdapter(getOrderList(), listener);

        recyclerView.setAdapter(perItemRecyclerAdapter);
    }

    private ArrayList<MenuItem> getOrderList() {
        // TODO:
        return new ArrayList<>(DummyContent.ITEMS);
    }

}