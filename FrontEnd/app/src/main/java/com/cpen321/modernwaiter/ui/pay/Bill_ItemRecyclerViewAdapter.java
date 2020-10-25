package com.cpen321.modernwaiter.ui.pay;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpen321.modernwaiter.Bill;
import com.cpen321.modernwaiter.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.cpen321.modernwaiter.Bill.Bill_item}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Bill_ItemRecyclerViewAdapter extends RecyclerView.Adapter<Bill_ItemRecyclerViewAdapter.ViewHolder> {

    private final List<Bill.Bill_item> mValues;

    public Bill_ItemRecyclerViewAdapter(List<Bill.Bill_item> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).item_number);
        holder.mContentView.setText(mValues.get(position).item_name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Bill.Bill_item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.name);
            mContentView = (TextView) view.findViewById(R.id.quantity);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}