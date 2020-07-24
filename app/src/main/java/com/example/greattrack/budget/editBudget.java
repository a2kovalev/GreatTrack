package com.example.greattrack.budget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greattrack.R;

import java.text.DecimalFormat;

public class editBudget extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BTAG", "editBudget onCreate method");
        setContentView(R.layout.edit_budget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        getSupportActionBar().setTitle("Edit Budget");
        Button createBudgetButton = findViewById(R.id.createBudgetButton);
        createBudgetButton.setText("Finish Edit");
        Spinner budgetSpinner = findViewById(R.id.budgetSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.budgetFreq, R.layout.budget_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(adapter);
        DecimalFormat df0 = new DecimalFormat("0.00");
        EditText amountEditText = findViewById(R.id.budgetNumberEditText);
        amountEditText.setText(df0.format(BudgetFragment.budget.getOriginalAmount()));

        String budgetFreqText = BudgetFragment.budget.getFrequency().toString();
        Log.d("BTAG", "budgetFreqText: " + budgetFreqText);
        String forSpinnerText = budgetFreqText.substring(0, 1).toUpperCase() + budgetFreqText.substring(1);
        Log.d("BTAG", "capitalised: " + forSpinnerText);

        budgetSpinner.setSelection(adapter.getPosition(forSpinnerText));

        createBudgetButton.setOnClickListener(v -> {
            DecimalFormat df = new DecimalFormat("0.00");
            Log.d("BTAG", "edit button clicked");
            if (!amountEditText.getText().toString().isEmpty()) {
                double amount = Double.parseDouble(amountEditText.getText().toString());
                amount = Double.parseDouble(df.format(amount));
                Log.d("BTAG", "budget amount: " + df.format(amount));
                BudgetFrequency frequency;
                String spinnerText = budgetSpinner.getSelectedItem().toString();
                if (spinnerText.equals("Daily")) {
                    Log.d("BTAG", "daily chosen");
                    frequency = BudgetFrequency.daily;
                } else if (spinnerText.equals("Weekly")) {
                    Log.d("BTAG", "weekly chosen");
                    frequency = BudgetFrequency.weekly;
                } else {
                    Log.d("BTAG", "monthly chosen");
                    frequency = BudgetFrequency.monthly;
                }
                BudgetFragment.budget.setAmount(amount);
                BudgetFragment.budget.setOriginalAmount(amount);
                BudgetFragment.budget.frequency = frequency;
                Log.d("BTAG", "new frequency: " + BudgetFragment.budget.getFrequency());
                finish();
            } else {
                Log.d("BTAG", "amount not edited");
                TextView selectWarning = new TextView(this);
                RelativeLayout.LayoutParams selectParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                selectWarning.setText("Please enter budget amount");
                selectWarning.setTextColor(Color.parseColor("#FF0000"));
                selectWarning.setTextSize((float) 16.0);
                selectParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                selectParams.setMargins(0, 5, 0, 5);
                selectParams.addRule(RelativeLayout.BELOW, budgetSpinner.getId());
                RelativeLayout relativeLayout = findViewById(R.id.budgetInnerRelativeLayout);
                relativeLayout.addView(selectWarning, selectParams);
            }
        });
    }
}
