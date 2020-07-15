package com.example.greattrack.budget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.greattrack.R;
import com.example.greattrack.habit.Habit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BudgetFragment extends Fragment {
    private static final String BTAG = "Budget Fragment";
    public static Budget budget;
    RelativeLayout relativeLayout;
    private boolean createButtonPresent = false;
    public static final String SHARED_PREFS = "NAME_OF_SP";
    public static final String NAME_OF_VAL = "budget";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_budget, container, false);
        relativeLayout = view.findViewById(R.id.budgetRelativeLayout);
        if (getBudget() != null) {
            budget = getBudget();
        }
        Button createBudgetButton = new Button(this.getActivity());
        CardView buttonCardView = new CardView(this.getActivity());
        RelativeLayout.LayoutParams buttonCardParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams createButtonParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        createBudgetButton.setText("Create your budget");
        createBudgetButton.setTextSize((float) 20.0);
        createBudgetButton.setStateListAnimator(null);
        createBudgetButton.setTextColor(Color.parseColor("#FFFFFF"));
        createBudgetButton.setBackgroundColor(Color.parseColor("#10cc3f"));
        buttonCardView.setCardBackgroundColor(Color.parseColor("#10cc3f"));
        createButtonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        createButtonParams.setMargins(15, 0, 15, 0);
        buttonCardView.setRadius((float) 50.0);
        buttonCardParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        buttonCardView.addView(createBudgetButton, createButtonParams);

        Log.d("BTAG", "There is a budget: " + (budget != null));

        if (budget == null) {
            relativeLayout.addView(buttonCardView, 0, buttonCardParams);
            createButtonPresent = true;

            createBudgetButton.setOnClickListener(v -> {
                Log.d("BTAG", "Create budget button clicked");
                createBudgetButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonCardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                goToCreateBudget();
            });
        } else {
            displayBudget();
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        saveBudget();
        displayBudget();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        saveBudget();
//    }

    public void displayBudget() {
        if(budget != null) {
            if(createButtonPresent) {
                relativeLayout.removeViewAt(0);
            }
            TextView tempText = new TextView(this.getActivity());
            tempText.setText("Current budget: " + budget.getAmount());

            relativeLayout.addView(tempText);
        }
    }

    public void goToCreateBudget() {
        Log.d("BTAG", "Go to create budget");
        Intent intent = new Intent(BudgetFragment.this.getActivity(), createBudget.class);
        startActivity(intent);
    }

    public void saveBudget()
    {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(budget);
        Log.d("BTAG", "JSON: " + json);
        editor.putString(NAME_OF_VAL, json);
        editor.apply();
    }

    public Budget getBudget()
    {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS, getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(NAME_OF_VAL, null);
        Log.d("BTAG", "gson from json: " + gson.fromJson(json, Budget.class));
        return gson.fromJson(json, Budget.class);
    }
}
