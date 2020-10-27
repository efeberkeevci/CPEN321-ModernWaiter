package com.cpen321.modernwaiter.ui.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

            TextView description = (TextView) itemView.findViewById(R.id.description);
            description.setText(mItem.description);

//            ImageView image = (ImageView) itemView.findViewById(R.id.image);
//            FirebaseStorage storage = FirebaseStorage.getInstance("gs://modern-waiter-47e96.appspot.com");
//            StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");
//
//            final long ONE_MEGABYTE = 1024 * 1024;
//            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    // Data for "images/island.jpg" is returns, use this as needed
//                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
//                            image.getHeight(), false));
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                    // Set placeholder image
//                }
//            });


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
}