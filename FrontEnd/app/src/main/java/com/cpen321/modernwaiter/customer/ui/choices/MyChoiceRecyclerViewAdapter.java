package com.cpen321.modernwaiter.customer.ui.choices;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.customer.application.MenuItem;
import com.cpen321.modernwaiter.customer.ui.menu.MenuRecyclerAdapter;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyChoiceRecyclerViewAdapter extends RecyclerView.Adapter<MyChoiceRecyclerViewAdapter.ViewHolder> {

    private final List<Chip> chipList;
    private final OnItemClickListener listener;

    public MyChoiceRecyclerViewAdapter(List<Chip> items, OnItemClickListener listener) {
        chipList = items;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_choice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.choiceItem = chipList.get(position);
        holder.bind(listener);
    }

    @Override
    public int getItemCount() {
        return chipList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public Chip choiceItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void bind(final OnItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(choiceItem);
                }
            });

            Chip choice = itemView.findViewById(R.id.chip);
            choice.setText(choiceItem.getText());
        }

    }
    public interface OnItemClickListener {
        void onItemClick( Chip item);
    }
}