package com.cpen321.modernwaiter.ui.payment.perItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.order.OrderItem;

import java.util.ArrayList;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;

public class PerItemRecyclerAdapter extends RecyclerView.Adapter<PerItemRecyclerAdapter.ViewHolder> {

    private ArrayList<OrderItem> orderList;
    private OnItemClickListener listener;

    public PerItemRecyclerAdapter(ArrayList<OrderItem> orderList, OnItemClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_split, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.menuItemBooleanPair = orderList.get(position);
        holder.bind(listener);
        holder.index = position;
    }

    @Override
    public int getItemCount() {
        // Return the sum of all values in the billMap = Total order
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int index;
        public View view;
        public OrderItem menuItemBooleanPair;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void bind(OnItemClickListener listener) {
            TextView nameTextView = view.findViewById(R.id.name);
            nameTextView.setText(menuItemBooleanPair.menuItem.name);

            TextView priceTextView = view.findViewById(R.id.price);
            priceTextView.setText(String.valueOf(menuItemBooleanPair.menuItem.cost));

            CheckBox checkBox = view.findViewById(R.id.checkbox);
            checkBox.setChecked(menuItemBooleanPair.selected);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked() != menuItemBooleanPair.selected) {
                        menuItemBooleanPair.setSelected(checkBox.isChecked());
                        listener.onItemClick(menuItemBooleanPair);
                    }
                }
            });

            System.out.println(this);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(OrderItem item);
    }
}
