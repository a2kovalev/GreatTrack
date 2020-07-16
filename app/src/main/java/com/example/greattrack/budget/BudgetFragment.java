package com.example.greattrack.budget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.greattrack.R;
import com.example.greattrack.habit.Habit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
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
        showCreateButton();
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

            CardView budgetCardView = new CardView(this.getActivity());
            RelativeLayout.LayoutParams budgetCardParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
            budgetCardParams.setMargins(100, 50, 100, 0);
            budgetCardView.setCardBackgroundColor(Color.parseColor("#10cc3f"));
            budgetCardView.setRadius((float) 50.0);
            budgetCardView.setStateListAnimator(null);
            RelativeLayout budgetCardRelativeLayout = new RelativeLayout(this.getActivity());
            RelativeLayout.LayoutParams budgetCardRelativeLayoutParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView budgetTextForCard = new TextView(this.getActivity());
            RelativeLayout.LayoutParams budgetCardTextParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            DecimalFormat df = new DecimalFormat("0.00");
            Log.d("BTAG", "amount: " + Double.parseDouble(df.format(budget.getAmount())));
            budgetTextForCard.setText("$" + df.format(budget.getAmount()));
            budgetTextForCard.setTextSize(45);
            budgetTextForCard.setTextColor(Color.parseColor("#FFFFFF"));
            budgetCardTextParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            budgetCardRelativeLayout.addView(budgetTextForCard, budgetCardTextParams);
            budgetCardView.addView(budgetCardRelativeLayout, budgetCardRelativeLayoutParams);
            budgetCardParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            relativeLayout.addView(budgetCardView, budgetCardParams);

            LinearLayout buttonLinearLayout = new LinearLayout(this.getActivity());
            buttonLinearLayout.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams buttonLinearLayoutParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button spentMoneyButton = new Button(this.getActivity());
            spentMoneyButton.setBackgroundResource(R.drawable.round_btn3);
            spentMoneyButton.setText("Subtract funds");
            spentMoneyButton.setTextSize(18);
            spentMoneyButton.setTextColor(Color.parseColor("#F3F3F3"));
            spentMoneyButton.setPadding(15, 0, 15 ,0);
            spentMoneyButton.setStateListAnimator(null);
            buttonLinearLayout.addView(spentMoneyButton);
            buttonLinearLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            spentMoneyButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("Subtract Funds");
                builder.setMessage("How much money did you spend?");
                LayoutInflater inflater = BudgetFragment.this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.subtract_alert_layout, null);
                final EditText subtractAmount = (EditText)mView.findViewById(R.id.subtractEditText);
                builder.setView(mView);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("BTAG", "positive button clicked");
                        double subtractThis = Double.parseDouble(subtractAmount.getText().toString());
                        Log.d("TAG", "subtracting " + subtractThis);
                        budget.subtractMoney(subtractThis);
                        displayBudget();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            });

            Button deleteButton = new Button(this.getActivity());
            RelativeLayout.LayoutParams deleteButtonParams =
                    new RelativeLayout.LayoutParams(650, ViewGroup.LayoutParams.WRAP_CONTENT);
            deleteButtonParams.setMargins(0, 25, 0, 10);
            deleteButton.setBackgroundResource(R.drawable.round_btn3);
            deleteButton.setText("Delete budget");
            deleteButton.setTextSize(18);
            deleteButton.setTextColor(Color.parseColor("#F3F3F3"));
            deleteButton.setPadding(15, 0, 15 ,0);
            deleteButton.setStateListAnimator(null);
            buttonLinearLayout.addView(deleteButton, deleteButtonParams);

            deleteButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("Delete Budget?");
                builder.setMessage("Are you sure that you would like to delete the budget? This cannot be undone.");

                builder.setPositiveButton("Yes, delete budget", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("BTAG", "positive button clicked");
                        budget = null;
                        saveBudget();
                        relativeLayout.removeAllViews();
                        showCreateButton();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            });

            buttonLinearLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);


            relativeLayout.addView(buttonLinearLayout, buttonLinearLayoutParams);
        }
    }

    public void showCreateButton() {
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
