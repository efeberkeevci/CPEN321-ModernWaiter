package com.cpen321.modernwaiter.customer.ui.choices;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.cpen321.modernwaiter.customer.application.MenuItem;
import com.cpen321.modernwaiter.customer.application.TableSession;
import com.cpen321.modernwaiter.customer.ui.choices.dummy.DummyContent;
import com.cpen321.modernwaiter.customer.ui.menu.DetailItemFragment;
import com.cpen321.modernwaiter.customer.ui.menu.MenuRecyclerAdapter;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cpen321.modernwaiter.customer.application.CustomerActivity.tableSession;

/**
 * A fragment representing a list of Items.
 */
public class ChoiceFragment extends Fragment {
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_choice_list, container, false);

        //set recycler
        RecyclerView recyclerView = view.findViewById(R.id.choice_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        MyChoiceRecyclerViewAdapter.OnItemClickListener listener = new MyChoiceRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chip chip) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                        // Handle the toggle.
                        if(isChecked){
                            List<String> session_choices_list = new ArrayList<>();
                            session_choices_list.addAll(tableSession.getChoices());
                            String option = chip.getText().toString();
                            if(!session_choices_list.contains(option)){
                                session_choices_list.add(option);
                                tableSession.putChoicesInBackend(session_choices_list);
                            }
                        }
                    }
                });
                fragmentTransaction.commit();
            }
        };
        ArrayList<Chip> chipItems = getChipList();
        MyChoiceRecyclerViewAdapter choiceRecyclerAdapter = new MyChoiceRecyclerViewAdapter(chipItems, listener);

        recyclerView.setAdapter(choiceRecyclerAdapter);
        return view;
    }

    private ArrayList<Chip> getChipList(){
        ArrayList<Chip> choices = new ArrayList<>();
        for(String name : ApiUtil.choices_name_list) {
            Chip chip = new Chip(view.getContext());
            chip.setText(name);
            choices.add(chip);
        }
        return choices;
    }

}