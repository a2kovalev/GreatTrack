package com.example.greattrack.budget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.greattrack.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class budgetLedger extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_ledger);
        DecimalFormat df = new DecimalFormat("0.00");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ledger");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#45bd00")));
        LinearLayout linearLayout = findViewById(R.id.ledgerLinearLayout);
        ArrayList<budgetDateAndTime> ledgerCopy = BudgetFragment.budgetLedgerList;
        Log.d("BTAG", "Ledger copy: ");
        if (ledgerCopy != null) {
            CardView initialAmountView = new CardView(this);
            TextView initialAmountText = new TextView(this);
            RelativeLayout initialRL = new RelativeLayout(this);
            RelativeLayout.LayoutParams initialRLParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams initialTextParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            initialAmountText.setText("Original amount: " + "$" + df.format(BudgetFragment.budget.originalAmount));
            initialAmountText.setTextSize(20);
            initialTextParams.setMargins(40, 0, 0, 0);
            initialRL.addView(initialAmountText, initialTextParams);
            initialAmountView.addView(initialRL, initialRLParams);
            linearLayout.addView(initialAmountView);
            for (budgetDateAndTime ledgerEntry : ledgerCopy) {
                RelativeLayout cardRelativeLayout = new RelativeLayout(this);
                RelativeLayout.LayoutParams cardRLParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                CardView cardView = new CardView(this);
                RelativeLayout.LayoutParams cardViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView dateOfAction = new TextView(this);
                dateOfAction.setId(View.generateViewId());
                TextView amountText = new TextView(this);
                RelativeLayout.LayoutParams amountTextParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams dateTextParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int month = ledgerEntry.getMonth();
                int day = ledgerEntry.getDay();
                int year = ledgerEntry.getYear();
                String hour = Integer.toString(ledgerEntry.getHour());
                if (ledgerEntry.getHour() < 10) {
                    hour = "0" + ledgerEntry.getHour();
                }
                String minute = Integer.toString(ledgerEntry.getMinute());
                if (ledgerEntry.getMinute() < 10) {
                    minute = "0" + ledgerEntry.getMinute();
                }
                double amount = ledgerEntry.getAmount();
                amountText.setText(df.format(amount));
                amountText.setTextSize(20);
                amountTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                amountTextParams.setMargins(15, 0, 40, 0);
                if (amount < 0) {
                    amountText.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    amountText.setTextColor(Color.parseColor("#45DB00"));
                    amountText.setText("+" + df.format(amount));
                    amountTextParams.setMargins(15, 0, 250, 0);
                }
                dateTextParams.setMargins(40, 0, 15, 0);
                String dateString = (month + "/" + day + "/" + year + " " + hour + ":" + minute);
                dateOfAction.setText(dateString);
                dateOfAction.setTextSize(20);
                cardRelativeLayout.addView(dateOfAction, dateTextParams);
                cardRelativeLayout.addView(amountText, amountTextParams);
                cardView.addView(cardRelativeLayout, cardRLParams);
                linearLayout.addView(cardView, cardViewParams);
            }
            CardView currentAmountView = new CardView(this);
            TextView currentAmountText = new TextView(this);
            RelativeLayout currentRL = new RelativeLayout(this);
            RelativeLayout.LayoutParams currentRLParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams currentTextParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            currentAmountText.setText("Current balance: " + "$" + df.format(BudgetFragment.budget.getAmount()));
            currentAmountText.setTextSize(20);
            currentTextParams.setMargins(40, 0, 0, 0);
            currentRL.addView(currentAmountText, currentTextParams);
            currentAmountView.addView(currentRL, currentRLParams);
            linearLayout.addView(currentAmountView);
        }
    }
}