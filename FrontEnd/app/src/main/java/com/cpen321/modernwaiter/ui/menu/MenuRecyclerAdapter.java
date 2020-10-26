package com.cpen321.modernwaiter.ui.menu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

;

public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.ViewHolder> {

    private final List<MenuItem> menuItems;
    private final OnItemClickListener listener;

    public MenuRecyclerAdapter(List<MenuItem> items, OnItemClickListener listener) {
        menuItems = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        retrieveMenuItems();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = menuItems.get(position);
        holder.bind(listener);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public MenuItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }

        public void bind(final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(mItem);
                    }
                }
            );

            TextView nameTextView = (TextView) itemView.findViewById(R.id.name);
            nameTextView.setText(mItem.name);

            TextView quantityTextView = (TextView) itemView.findViewById(R.id.quantity);
            quantityTextView.setText(mItem.quantity);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    private List<MenuItem> retrieveMenuItems(){
        Log.i("NOTE:","retrieving menu items");

        //RequestQueue queue = Volley.newRequestQueue(getActivity());
        LinkedList<MenuItem> response_menu_items = new LinkedList<MenuItem>();
        String url ="http://52.188.158.129:3000/items/1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response:",response);

                        //Toast.makeText(TimeActivity.this,"Date: "+(response),Toast.LENGTH_LONG).show();
                        //time_text = findViewById(R.id.time_text);
                        //time_text.setText("Date: "+(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asd",error.toString());
                //Toast.makeText(TimeActivity.this,("Error"),Toast.LENGTH_LONG);
            }
        });
       // queue.add(stringRequest);

        return response_menu_items;
    }
    private void menuItemParser(String json){
        try {
            // convert JSON array to Java List
            List<MenuItem> menu_items = new Gson().fromJson(json, new TypeToken<List<MenuItem>>() {}.getType());

            // print list of users
            menu_items.forEach(System.out::println);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}