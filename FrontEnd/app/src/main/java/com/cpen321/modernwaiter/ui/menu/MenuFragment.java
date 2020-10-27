package com.cpen321.modernwaiter.ui.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.cpen321.modernwaiter.ui.order.OrderFragment;
import com.cpen321.modernwaiter.ui.pay.BillFragment;

/**
 * A fragment representing a list of Items.
 */
public class MenuFragment extends Fragment {

    MenuRecyclerAdapter menuRecyclerAdapter;
    View view;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_menu, container, false);

        refresh();
        return view;
    }

    public void refresh() {

        Button viewCartButton = view.findViewById(R.id.viewCartButton);
        viewCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_menu_to_navigation_order);
            }
        });

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MenuRecyclerAdapter.OnItemClickListener listener = new MenuRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                DetailItemFragment detailItemFragment = new DetailItemFragment(item, menuRecyclerAdapter);
                fragmentTransaction.add(R.id.fragment_menu, detailItemFragment);

                fragmentTransaction.commit();
            }
        };

        MainActivity mainActivity = (MainActivity) getActivity();
        menuRecyclerAdapter = new MenuRecyclerAdapter(mainActivity.tableSession.getMenuItems(), listener);


        if (mainActivity.tableSession.getFeatureItem() != null) {
            recyclerView.setAdapter(menuRecyclerAdapter);
            TextView featureNameView = view.findViewById(R.id.feature_name);
            featureNameView.setText("Recommended: " + mainActivity.tableSession.getFeatureItem().name);

            TextView featureDescView = view.findViewById(R.id.feature_description);
            featureDescView.setText(mainActivity.tableSession.getFeatureItem().description);

            CardView featureCardView = view.findViewById(R.id.featureItem);
            featureCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    DetailItemFragment detailItemFragment = new DetailItemFragment(mainActivity.tableSession.getFeatureItem(), null);
                    fragmentTransaction.add(R.id.fragment_menu, detailItemFragment);

                    fragmentTransaction.commit();
                }
            });
        }
    }
}