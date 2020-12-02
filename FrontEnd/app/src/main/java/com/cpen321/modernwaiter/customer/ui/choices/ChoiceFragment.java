package com.cpen321.modernwaiter.customer.ui.choices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cpen321.modernwaiter.R;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private ArrayList<Chip> allChips = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_choice_list, container, false);

        requestQueue = Volley.newRequestQueue(requireContext());

        fetchChoiceList();

        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void populateChipList(KeywordsList keywordsList){
        LinearLayout scrollView = (LinearLayout) view.findViewById(R.id.scrollView);



        TextView tasteLabel = new TextView(requireContext());
        tasteLabel.setText("\nFavorite Tastes\n");
        tasteLabel.setTextSize(20);
        tasteLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scrollView.addView(tasteLabel);
        scrollView.addView(generateChipGroup(keywordsList.food_taste));

        TextView proteinLabel = new TextView(requireContext());
        proteinLabel.setText("\nFavorite Proteins\n");
        proteinLabel.setTextSize(20);
        proteinLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scrollView.addView(proteinLabel);
        scrollView.addView(generateChipGroup(keywordsList.proteins));

        TextView drinkLabel = new TextView(requireContext());
        drinkLabel.setText("\nFavorite Drinks\n");
        drinkLabel.setTextSize(20);
        drinkLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scrollView.addView(drinkLabel);
        scrollView.addView(generateChipGroup(keywordsList.drinks));

        TextView verbLabel = new TextView(requireContext());
        verbLabel.setText("\nCooking Method\n");
        verbLabel.setTextSize(20);
        verbLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scrollView.addView(verbLabel);
        scrollView.addView(generateChipGroup(keywordsList.food_verbs));

        TextView optionLabel = new TextView(requireContext());
        optionLabel.setText("\nDietary Restrictions\n");
        optionLabel.setTextSize(20);
        optionLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scrollView.addView(optionLabel);
        scrollView.addView(generateChipGroup(keywordsList.food_options));
    }

    private ChipGroup generateChipGroup(ArrayList<String> stringList) {
        ChipGroup chipGroup = new ChipGroup(requireContext());

        for(String name : stringList) {
            Chip chip = new Chip(view.getContext());
            chip.setText("   " + name + "    ");
            chip.setCheckable(true);
            chip.setChecked(tableSession.getUserPreference().contains(name));

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String text = chip.getText().toString();
                if (isChecked) {
                    tableSession.getUserPreference().add(name);
                    chip.setText(text.replaceAll(" ", ""));
                } else {
                    tableSession.getUserPreference().remove(name);
                    chip.setText("   " + text + "    ");
                }

                ArrayList<String> choiceList = new ArrayList<>();
                for (Chip chipIndex : allChips) {
                    if (chipIndex.isChecked()) {
                        choiceList.add(chipIndex.getText().toString().replaceAll(" ", ""));
                    }
                }

                postUserPreference(choiceList);
            });


            chipGroup.addView(chip);
            allChips.add(chip);
        }

        return chipGroup;
    }

    private void postUserPreference(List<String> choices_list) {

        StringBuilder preference = new StringBuilder();
        for (String choice : choices_list) {
            preference.append(" ").append(choice);
        }
        if (preference.length() != 0)
            preference = new StringBuilder(preference.substring(1));

        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("userId", "" + tableSession.getUserId());
        bodyFields.put("preferences", preference.toString());

        final String bodyJSON = new Gson().toJson(bodyFields);
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, ApiUtil.choices,
                response -> tableSession.fetchUserRecommendation(),

                error -> Log.i("Post preference", error.toString())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return bodyJSON.getBytes();
            }
        };

        requestQueue.add(stringRequest);
    }

    private void fetchChoiceList() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiUtil.recommendationKeyword,
                response -> {
                    KeywordsWrapper keywordsWrapper =  new Gson().fromJson(response, new TypeToken<KeywordsWrapper>() {}.getType());
                    populateChipList(keywordsWrapper.keywords);
                }, error -> Log.i("Fetch choice list", error.toString()));

        requestQueue.add(stringRequest);
    }

    private class KeywordsWrapper {
        public KeywordsList keywords;
    }
    private class KeywordsList {
        public ArrayList<String> food_verbs;
        public ArrayList<String> food_taste;
        public ArrayList<String> food_options;
        public ArrayList<String> proteins;
        public ArrayList<String> drinks;
    }
}