package com.cpen321.modernwaiter.ui.payment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.bill.BillRecyclerAdapter;
import com.stripe.android.view.CardInputWidget;

import java.text.DecimalFormat;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;


public class StripePaymentFragment extends Fragment {

    /**
     * This example collects card payments, implementing the guide here: https://stripe.com/docs/payments/accept-a-payment-synchronously#android
     */
    private int totalAmount = 0;
    private StripePaymentController stripePaymentController;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stripe, container, false);

        getPaymentAmount();

        stripePaymentController = new StripePaymentController(requireActivity(), totalAmount);

        loadBillRecycler();
        loadCardInputWidget();
        loadPaymentButton();
        return view;
    }

    private void getPaymentAmount() {
        totalAmount = 0;
        tableSession.getBill().forEach(((menuItem, count) ->
                totalAmount += menuItem.getCost() * count));
    }

    private void loadBillRecycler() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stripe_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        BillRecyclerAdapter billRecyclerAdapter = new BillRecyclerAdapter(tableSession.getBill());
        recyclerView.setAdapter(billRecyclerAdapter);
    }

    private void loadCardInputWidget() {
        // Clear the card widget
        CardInputWidget cardInputWidget = view.findViewById(R.id.cardInputWidget);
        cardInputWidget.clear();

    }

    private void loadPaymentButton() {
        Button payButton = view.findViewById(R.id.payButton);
        String amountText = "Pay $" + new DecimalFormat("#.##")
                .format((double) totalAmount / 100);
        payButton.setText(amountText);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardInputWidget cardInputWidget = view.findViewById(R.id.cardInputWidget);
                if(cardInputWidget.getPaymentMethodCreateParams() == null){
                    //TODO: add a toast to ask the user to input details before pay if input is null
                    Log.d("Alert: ", "Not implemented yet if params == null");
                }
                stripePaymentController.pay(cardInputWidget.getPaymentMethodCreateParams());
            }
        });
    }
}
