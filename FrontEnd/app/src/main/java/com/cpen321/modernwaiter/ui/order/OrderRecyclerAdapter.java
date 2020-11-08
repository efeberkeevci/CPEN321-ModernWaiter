package com.cpen321.modernwaiter.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.application.MenuItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MenuItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    public final ArrayList<MenuItem> itemArray;
    private final OnItemClickListener listener;

    public OrderRecyclerAdapter(HashMap<MenuItem, Integer> orderMap, OnItemClickListener listener) {
        this.listener = listener;

        // Add all the items that have been ordered atleast once
        itemArray = orderMap.keySet().stream()
                .filter(menuItem -> orderMap.get(menuItem) > 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.menuItem = itemArray.get(position);
        holder.bind(listener);
    }

    @Override
    public int getItemCount() {
        return itemArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public MenuItem menuItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void bind(OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(menuItem);
                    }
                }
            );

            TextView nameView = view.findViewById(R.id.name);
            nameView.setText(menuItem.name);

            TextView quantityView = view.findViewById(R.id.quantity);
            quantityView.setText(menuItem.quantity);

            TextView priceView = view.findViewById(R.id.price);
            priceView.setText(menuItem.getTotalCartPriceString());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }
}