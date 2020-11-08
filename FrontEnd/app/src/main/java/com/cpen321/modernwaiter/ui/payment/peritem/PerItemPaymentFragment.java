package com.cpen321.modernwaiter.ui.payment.peritem;

//import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;

import java.text.DecimalFormat;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;

public class PerItemPaymentFragment extends Fragment {

    private PerItemRecyclerAdapter perItemRecyclerAdapter;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_per_item_payment, container, false);

        PerItemRecyclerAdapter.OnItemClickListener listener = new PerItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PaymentItem item) {
                // TODO: PERFORM CALL TO DATABASE & NOTIFY I CHOOSE OR DESELECT

                tableSession.updateItemSelected(item);
                perItemRecyclerAdapter.notifyDataSetChanged();
                updateAmount();
            }
        };

        RecyclerView recyclerView = view.findViewById(R.id.per_item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        perItemRecyclerAdapter = new PerItemRecyclerAdapter(tableSession.getOrderList(), listener);
        recyclerView.setAdapter(perItemRecyclerAdapter);

        Button payButton = view.findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_per_item_payment_to_stripe);
            }
        });
        updateAmount();

        return view;
    }

    public void updateAmount() {
        int amount = tableSession.getOrderList().stream()
                .filter(menuItemBooleanPair -> menuItemBooleanPair.selected)
                .reduce(0, (subtotal, menuItemBooleanPair) -> subtotal + menuItemBooleanPair.menuItem.getCost(), Integer::sum);

        String amountText = "Pay $" + new DecimalFormat("#.##")
                .format((double) amount / 100);

        Button payButton = view.findViewById(R.id.payButton);
        payButton.setText(amountText);
    }
}