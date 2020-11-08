package com.cpen321.modernwaiter.ui.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.application.MenuItem;
import com.squareup.picasso.Picasso;

public class DetailItemFragment extends Fragment {

    private final MenuItem menuItem;
    private TextView quantityText;
    private final Fragment thisFragment = this;
    private final RecyclerView.Adapter adapter;
    private Button decrementButton;

    public DetailItemFragment(MenuItem menuItem, RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        this.menuItem = menuItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_item, container, false);

        TextView nameTextView = view.findViewById(R.id.name);
        nameTextView.setText(menuItem.name);

        TextView descriptionTextView = view.findViewById(R.id.description);
        descriptionTextView.setText(menuItem.description);

        TextView priceTextView = view.findViewById(R.id.price);
        priceTextView.setText(menuItem.getPriceString());

        Button incrementButton = view.findViewById(R.id.incrementButton);
        decrementButton = view.findViewById(R.id.decrementButton);

        quantityText = view.findViewById(R.id.quantity);
        updateQuantity();

        ImageView imageView = view.findViewById(R.id.image);
        Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/modern-waiter-47e96.appspot.com/o/dummy-ceviche.jpg?alt=media")
                .into(imageView);

        incrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuItem.incrementQuantity();
                    updateQuantity();
                }
            });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItem.decrementQuantity();
                updateQuantity();
            }
        });

        ImageButton exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

                fragmentTransaction.remove(thisFragment);
                fragmentTransaction.commit();
            }
        });

        // Removing this means that the user can click the menu thats in the background
        view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Alert:", "Empty onclick method");
                }
            }
        );

        return view;
    }

    public void updateQuantity() {
        if ("0".equals(menuItem.quantity)) {
            decrementButton.setVisibility(View.INVISIBLE);
        } else {
            decrementButton.setVisibility(View.VISIBLE);
        }

        quantityText.setText(menuItem.quantity);
    }
}
