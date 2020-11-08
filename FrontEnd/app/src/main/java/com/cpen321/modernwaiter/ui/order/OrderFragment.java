package com.cpen321.modernwaiter.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.application.MainActivity;
import com.cpen321.modernwaiter.application.MenuItem;
import com.cpen321.modernwaiter.ui.menu.DetailItemFragment;

import java.util.HashMap;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;

public class OrderFragment extends Fragment {

    private OrderRecyclerAdapter orderRecyclerAdapter;
    private Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        Button startBillButton = view.findViewById(R.id.startBillButton);
        startBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_order_to_navigation_bill);
            }
        });

        OrderRecyclerAdapter.OnItemClickListener listener = new OrderRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                DetailItemFragment detailItemFragment = new DetailItemFragment(item, orderRecyclerAdapter);
                fragmentTransaction.add(R.id.fragment_order, detailItemFragment);

                fragmentTransaction.commit();
            }
        };

        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.order_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        HashMap<MenuItem, Integer> billMap = MainActivity.tableSession.getCart();
        orderRecyclerAdapter = new OrderRecyclerAdapter(billMap, listener);

        recyclerView.setAdapter(orderRecyclerAdapter);

        Button checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderRecyclerAdapter.itemArray.isEmpty()) {
                    if (toast != null) {
                        toast.cancel();
                    }

                    int duration = Toast.LENGTH_SHORT;

                    toast = Toast.makeText(requireActivity(), "Please add item before checkout", duration);
                    toast.show();

                    return;
                }

                tableSession.checkout();
                while (orderRecyclerAdapter.itemArray.size() != 0) {
                    orderRecyclerAdapter.itemArray.remove(0);
                    orderRecyclerAdapter.notifyItemRemoved(0);
                    orderRecyclerAdapter.notifyItemRangeChanged(0, orderRecyclerAdapter.itemArray.size());
                }
            }
        });

        return view;
    }
}