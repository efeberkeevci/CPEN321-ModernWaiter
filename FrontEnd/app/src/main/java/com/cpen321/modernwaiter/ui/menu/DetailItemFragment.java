package com.cpen321.modernwaiter.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.application.MenuItem;

public class DetailItemFragment extends Fragment {

    MenuItem menuItem;
    TextView quantityText;
    Fragment thisFragment = this;
    MenuRecyclerAdapter adapter;

    DetailItemFragment(MenuItem menuItem, MenuRecyclerAdapter adapter) {
        this.adapter = adapter;
        this.menuItem = menuItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_item, container, false);

        quantityText = view.findViewById(R.id.quantity);
        updateQuantity();

        TextView nameTextView = view.findViewById(R.id.name);
        nameTextView.setText(menuItem.name);

        TextView descriptionTextView = view.findViewById(R.id.description);
        descriptionTextView.setText(menuItem.description);

        TextView priceTextView = view.findViewById(R.id.price);
        priceTextView.setText(menuItem.getPriceString());

        Button incrementButton = view.findViewById(R.id.incrementButton);
        Button decrementButton = view.findViewById(R.id.decrementButton);

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

                if (adapter != null)
                    adapter.notifyDataSetChanged();

                fragmentTransaction.remove(thisFragment);
                fragmentTransaction.commit();
            }
        });

        // Removing this means that the user can click the menu thats in the background
        view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }
        );

        return view;
    }

    public void updateQuantity() {
        quantityText.setText(menuItem.quantity);
    }
}
