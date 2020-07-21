package com.example.greattrack.budget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.greattrack.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class budgetLedger extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_ledger);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ledger");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#45bd00")));
        LinearLayout linearLayout = findViewById(R.id.ledgerLinearLayout);
        ArrayList<budgetDateAndTime> ledgerCopy = BudgetFragment.budgetLedgerList;
        Log.d("BTAG", "Ledger copy: ");
        if (ledgerCopy != null) {
            for (budgetDateAndTime ledgerEntry : ledgerCopy) {
                CardView cardView = new CardView(this);
                RelativeLayout.LayoutParams cardViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView dateOfAction = new TextView(this);
                int month = ledgerEntry.getMonth();
                int day = ledgerEntry.getDay();
                int year = ledgerEntry.getYear();
                int hour = ledgerEntry.getHour();
                int minute = ledgerEntry.getMinute();
                String dateString = (month + "/" + day + "/" + year + " " + hour + ":" + minute);
                dateOfAction.setText(dateString);
                dateOfAction.setTextSize(20);
                cardView.addView(dateOfAction);
                linearLayout.addView(cardView, cardViewParams);
            }
        }
    }
}