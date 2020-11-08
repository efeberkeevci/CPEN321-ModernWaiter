package com.cpen321.modernwaiter.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.application.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;

public class BillFragment extends Fragment {

    private Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = view.findViewById(R.id.bill_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        HashMap<MenuItem, Integer> billMap = tableSession.getBill();

        BillRecyclerAdapter billRecyclerAdapter = new BillRecyclerAdapter(billMap);
        recyclerView.setAdapter(billRecyclerAdapter);


        Button startPaymentButton = view.findViewById(R.id.startPaymentButton);

        startPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billRecyclerAdapter.isEmpty()) {
                    if (toast != null) {
                        toast.cancel();
                    }

                    int duration = Toast.LENGTH_SHORT;

                    toast = Toast.makeText(requireActivity(), "Please order an item before payment", duration);
                    toast.show();

                    return;
                }

                Navigation.findNavController(view).navigate(R.id.action_navigation_bill_to_navigation_payment);
            }
        });

        return view;
    }

}