package com.cpen321.modernwaiter.ui.pay;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.cpen321.modernwaiter.ui.MenuItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BillRecyclerAdapter extends RecyclerView.Adapter<BillRecyclerAdapter.ViewHolder> {

    private final HashMap<MenuItem, Integer> billMap;
    private final ArrayList<MenuItem> itemArray;

    public BillRecyclerAdapter(HashMap<MenuItem, Integer> billMap) {
        this.billMap = billMap;

        // Add all the items that have been ordered atleast once
        itemArray = new ArrayList<MenuItem>(billMap.keySet().stream()
            .filter(menuItem -> billMap.get(menuItem) > 0)
            .collect(Collectors.toList())
        );
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MenuItem menuItem = itemArray.get(position);

        holder.nameView.setText(menuItem.name);
        holder.quantityView.setText(String.valueOf(billMap.get(menuItem)));

        holder.priceView.setText(menuItem.getTotalPriceString());
    }

    @Override
    public int getItemCount() {
        return itemArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final TextView quantityView;
        public final TextView priceView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.name);
            quantityView = (TextView) view.findViewById(R.id.quantity);
            priceView = (TextView) view.findViewById(R.id.price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + quantityView.getText() + "'";
        }
    }
}