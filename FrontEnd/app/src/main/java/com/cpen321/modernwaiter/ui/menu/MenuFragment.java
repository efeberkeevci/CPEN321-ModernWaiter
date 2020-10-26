package com.cpen321.modernwaiter.ui.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpen321.modernwaiter.MainActivity;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.ui.MenuItem;

/**
 * A fragment representing a list of Items.
 */
public class MenuFragment extends Fragment {

    MenuRecyclerAdapter menuRecyclerAdapter;

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
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

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

        recyclerView.setAdapter(menuRecyclerAdapter);

        return view;
    }
}