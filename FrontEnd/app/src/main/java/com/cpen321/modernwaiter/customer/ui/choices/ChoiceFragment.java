package com.cpen321.modernwaiter.customer.ui.choices;

import android.annotation.SuppressLint;
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
import com.cpen321.modernwaiter.customer.ui.menu.DetailItemFragment;
import com.cpen321.modernwaiter.customer.ui.menu.MenuRecyclerAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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
        ChipGroup chipGroup = view.findViewById(R.id.chip_group);

        for (Chip chip : getChipList()) {
            chipGroup.addView(chip);
        }

        return view;
    }

    @SuppressLint("ResourceAsColor")
    private ArrayList<Chip> getChipList(){
        ArrayList<Chip> choices = new ArrayList<>();
        for(String name : ApiUtil.choices_name_list) {
            Chip chip = new Chip(view.getContext());
            chip.setText("   " + name + "    ");
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String text = chip.getText().toString();
                if (isChecked) {
                    chip.setText(text.replaceAll(" ", ""));
                } else {
                    chip.setText("   " + text + "    ");
                }

                ArrayList<String> choiceList = new ArrayList<>();
                for (Chip chipIndex : choices) {
                    if (chipIndex.isChecked()) {
                        choiceList.add(chipIndex.getText().toString().replaceAll(" ", ""));
                    }
                }

                tableSession.putChoicesInBackend(choiceList);
            });
            choices.add(chip);
        }
        return choices;
    }

}