package akov.example.greattrack.budget;

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

import akov.example.greattrack.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

public class createBudget extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale.setDefault(new Locale("en", "US"));
        Log.d("BTAG", "budget onCreate method");
        setContentView(R.layout.create_budget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        getSupportActionBar().setTitle("Create Budget");
        Button createBudgetButton = findViewById(R.id.createBudgetButton);
        Spinner budgetSpinner = findViewById(R.id.budgetSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.budgetFreq, R.layout.budget_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(adapter);

        createBudgetButton.setOnClickListener(v -> {
            DecimalFormat df = new DecimalFormat("0.00");
            Log.d("BTAG", "create button clicked");
            EditText amountEditText = findViewById(R.id.budgetNumberEditText);
            if (!amountEditText.getText().toString().equals("Enter Amount")) {
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
                LocalDateTime currDateTime = LocalDateTime.now();
                BudgetFragment.budget = new Budget(amount, frequency);
                BudgetFragment.lastResetDate = currDateTime;
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
