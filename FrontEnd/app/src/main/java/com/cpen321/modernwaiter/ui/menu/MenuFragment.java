package com.cpen321.modernwaiter.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.application.MenuItem;

import java.util.ArrayList;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;

/**
 * A fragment representing a list of Items.
 */
public class MenuFragment extends Fragment {

    private MenuRecyclerAdapter menuRecyclerAdapter;
    private View view;

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
        RecyclerView recyclerView = view.findViewById(R.id.menu_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        MenuRecyclerAdapter.OnItemClickListener listener = new MenuRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                DetailItemFragment detailItemFragment = new DetailItemFragment(item, menuRecyclerAdapter);
                fragmentTransaction.add(R.id.fragment_menu, detailItemFragment);

                fragmentTransaction.commit();
            }
        };

        ArrayList<MenuItem> menuItems = new ArrayList<>(tableSession.getMenuItems());
        System.out.println(menuItems);
        menuRecyclerAdapter = new MenuRecyclerAdapter(menuItems, listener);

        recyclerView.setAdapter(menuRecyclerAdapter);

        if (tableSession.getFeatureItem() != null) {
           // recyclerView.setAdapter(menuRecyclerAdapter);
            TextView featureNameView = view.findViewById(R.id.feature_name);
            String featureItemText = "Special: " + tableSession.getFeatureItem().name;

            featureNameView.setText(featureItemText);

            TextView featureDescView = view.findViewById(R.id.feature_description);
            featureDescView.setText(tableSession.getFeatureItem().description);

            CardView featureCardView = view.findViewById(R.id.featureItem);
            featureCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    DetailItemFragment detailItemFragment = new DetailItemFragment(tableSession.getFeatureItem(), null);
                    fragmentTransaction.add(R.id.fragment_menu, detailItemFragment);

                    fragmentTransaction.commit();
                }
            });
        }
    }
}