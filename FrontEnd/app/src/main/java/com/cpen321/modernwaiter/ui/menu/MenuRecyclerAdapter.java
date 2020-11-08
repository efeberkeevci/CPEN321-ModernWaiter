package com.cpen321.modernwaiter.ui.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.application.MenuItem;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.ViewHolder> {

    private final List<MenuItem> menuItems;
    private final OnItemClickListener listener;

    public MenuRecyclerAdapter(List<MenuItem> items, OnItemClickListener listener) {
        menuItems = items;
        this.listener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.menuItem = menuItems.get(position);
        holder.bind(listener);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public MenuItem menuItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void bind(final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(menuItem);
                    }
                }
            );

            TextView nameTextView = itemView.findViewById(R.id.name);
            nameTextView.setText(menuItem.name);

            TextView description = itemView.findViewById(R.id.description);
            description.setText(menuItem.description);

            TextView priceView = itemView.findViewById(R.id.price);
            priceView.setText(menuItem.getPriceString());

            TextView quantityTextView = itemView.findViewById(R.id.quantity);
            quantityTextView.setText(menuItem.quantity);

            ImageView imageView = itemView.findViewById(R.id.image);
            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/modern-waiter-47e96.appspot.com/o/dummy-ceviche.jpg?alt=media")
                    .into(imageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }
}