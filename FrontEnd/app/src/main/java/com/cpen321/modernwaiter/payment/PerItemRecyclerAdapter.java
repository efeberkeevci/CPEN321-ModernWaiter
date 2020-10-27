package com.cpen321.modernwaiter.payment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;

import java.util.ArrayList;

public class PerItemRecyclerAdapter extends RecyclerView.Adapter<PerItemRecyclerAdapter.ViewHolder> {

    private final ArrayList<MenuItem> orderList;
    private final OnItemClickListener listener;

    public PerItemRecyclerAdapter(ArrayList<MenuItem> orderList, OnItemClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.split_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.menuItem = orderList.get(position);
        holder.bind(listener);
    }

    @Override
    public int getItemCount() {
        // Return the sum of all values in the billMap = Total order
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public MenuItem menuItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void bind(final OnItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(menuItem);
                }
            });

            TextView nameTextView = (TextView) view.findViewById(R.id.name);
            nameTextView.setText(menuItem.name);

            TextView priceTextView = (TextView) view.findViewById(R.id.price);
            priceTextView.setText(String.valueOf(menuItem.cost));

            System.out.println(this);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }
}
