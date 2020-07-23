package com.example.greattrack.budget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.greattrack.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BudgetFragment extends Fragment {
    private static final String BTAG = "Budget Fragment";
    public static Budget budget;
    RelativeLayout relativeLayout;
    private boolean createButtonPresent = false;
    public static final String SHARED_PREFS = "NAME_OF_SP";
    public static final String NAME_OF_VAL = "budget";
    public static final String LEDGER_PREFS = "LEDGER_PREF";
    public static final String NAME_OF_LED = "ledger";
    public static ArrayList<budgetDateAndTime> budgetLedgerList = new ArrayList<>();
    public static LocalDateTime lastResetDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_budget, container, false);
        relativeLayout = view.findViewById(R.id.budgetRelativeLayout);

        if (getBudget() != null) {
            budget = getBudget();
        }
        if (getLedger() != null) {
            budgetLedgerList = getLedger();
            Log.d("BTAG", "budget ledger at start: " + budgetLedgerList);
        }
        if (loadResetDate() != 0) {
            lastResetDate = Instant.ofEpochMilli(loadResetDate()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        if (lastResetDate != null) {
            budgetReset();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveBudget();
        saveLedger();
        saveResetDate();
    }

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
                    new RelativeLayout.LayoutParams(700, ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonLinearLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            TextView haveYouSpentText = new TextView(this.getActivity());
            RelativeLayout.LayoutParams haveYouSpentParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            haveYouSpentParams.setMargins(0, 5, 0, 5);
            haveYouSpentText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            haveYouSpentText.setTextSize((float) 15.0);
            haveYouSpentText.setText("Spent money?");
            haveYouSpentText.setTextColor(getResources().getColor(R.color.colorAccent));
            buttonLinearLayout.addView(haveYouSpentText, haveYouSpentParams);

            Button spentMoneyButton = new Button(this.getActivity());
            spentMoneyButton.setBackgroundResource(R.drawable.round_btn3);
            spentMoneyButton.setText("Subtract funds");
            spentMoneyButton.setTextSize(16);
            spentMoneyButton.setTextColor(Color.parseColor("#FFFFFF"));
            spentMoneyButton.setPadding(5, 0, 5 ,0);
            spentMoneyButton.setStateListAnimator(null);
            buttonLinearLayout.addView(spentMoneyButton);

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
                        saveBudget();
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

            TextView needMoreText = new TextView(this.getActivity());
            RelativeLayout.LayoutParams needMoreParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            needMoreParams.setMargins(0, 5, 0, 5);
            needMoreText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            needMoreText.setTextSize((float) 15.0);
            needMoreText.setText("Need a little more?");
            needMoreText.setTextColor(getResources().getColor(R.color.colorAccent));
            buttonLinearLayout.addView(needMoreText, needMoreParams);

            Button addMoneyButton = new Button(this.getActivity());
            addMoneyButton.setBackgroundResource(R.drawable.round_btn3);
            addMoneyButton.setText("Add funds");
            addMoneyButton.setTextSize(16);
            addMoneyButton.setTextColor(Color.parseColor("#FFFFFF"));
            addMoneyButton.setPadding(5, 0, 5 ,0);
            addMoneyButton.setStateListAnimator(null);
            buttonLinearLayout.addView(addMoneyButton);

            addMoneyButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("Add Funds");
                builder.setMessage("How much money would you like to add?");
                LayoutInflater inflater = BudgetFragment.this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.subtract_alert_layout, null);
                final EditText subtractAmount = (EditText)mView.findViewById(R.id.subtractEditText);
                builder.setView(mView);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("BTAG", "positive button clicked");
                        double addThis = Double.parseDouble(subtractAmount.getText().toString());
                        Log.d("TAG", "adding " + addThis);
                        budget.addMoney(addThis);
                        saveBudget();
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

            LinearLayout horizontalLinearLayout = new LinearLayout(this.getActivity());
            RelativeLayout.LayoutParams horizontalLinearParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            horizontalLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            Button deleteButton = new Button(this.getActivity());
            deleteButton.setId(View.generateViewId());
            RelativeLayout.LayoutParams deleteButtonParams =
                    new RelativeLayout.LayoutParams(200, 200);
            deleteButtonParams.setMargins(0, 15, 0, 5);
            deleteButton.setBackgroundResource(R.drawable.round_btn4);
            deleteButton.setPadding(5, 0, 5 ,0);
            deleteButton.setStateListAnimator(null);
            horizontalLinearLayout.addView(deleteButton, deleteButtonParams);

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
                        budgetLedgerList.clear();
                        saveLedger();
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

            Button editButton = new Button(this.getActivity());
            editButton.setId(View.generateViewId());
            RelativeLayout.LayoutParams editButtonParams =
                    new RelativeLayout.LayoutParams(200, 200);
            editButtonParams.setMargins(10, 15, 0, 5);
            editButton.setBackgroundResource(R.drawable.round_btn5);
            editButton.setPadding(5, 5, 5 ,5);
            editButton.setStateListAnimator(null);
            editButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            editButton.setOnClickListener(v -> {
                goToEditBudget();
            });

            Button historyButton = new Button(this.getActivity());
            RelativeLayout.LayoutParams historyButtonParams =
                    new RelativeLayout.LayoutParams(200, 200);
            historyButtonParams.setMargins(10, 15, 0, 5);
            historyButton.setBackgroundResource(R.drawable.round_btn6);
            historyButton.setPadding(5, 5, 5 ,5);
            historyButton.setStateListAnimator(null);
            historyButtonParams.addRule(RelativeLayout.LEFT_OF, editButton.getId());
            historyButtonParams.addRule(RelativeLayout.RIGHT_OF, deleteButton.getId());

            historyButton.setOnClickListener(v -> {
                goToLedger();
            });

            horizontalLinearLayout.addView(historyButton,  historyButtonParams);
            horizontalLinearLayout.addView(editButton,  editButtonParams);

            horizontalLinearParams.setMargins(0, 20, 0, 0);
            horizontalLinearParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            buttonLinearLayout.addView(horizontalLinearLayout, horizontalLinearParams);
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

    private void budgetReset() {
        LocalDateTime currDate = LocalDateTime.now();
        LocalDateTime lastReset = lastResetDate;
        long currTimeInMillis = currDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Log.d("BTAG", "current millis time: " + currTimeInMillis);
        long oldTimeInMillis = lastReset.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Log.d("BTAG", "last reset millis time: " + oldTimeInMillis);

        if (budget.getFrequency() == BudgetFrequency.daily) {
            long twentyFourHours = TimeUnit.HOURS.toMillis(24);
            if (currTimeInMillis - oldTimeInMillis >= twentyFourHours) {
                double original = budget.getOriginalAmount();
                budget.setAmount(original);
                lastResetDate = currDate;
            }
        } else if (budget.getFrequency() == BudgetFrequency.weekly) {
            long oneWeek = TimeUnit.DAYS.toMillis(7);
            if (currTimeInMillis - oldTimeInMillis >= oneWeek) {
                double original = budget.getOriginalAmount();
                budget.setAmount(original);
                lastResetDate = currDate;
            }
        } else {
            if (currDate.getMonthValue() != lastReset.getMonthValue()) {
                double original = budget.getOriginalAmount();
                budget.setAmount(original);
                lastResetDate = currDate;
            }
        }
    }

    public void goToCreateBudget() {
        Log.d("BTAG", "Go to create budget");
        Intent intent = new Intent(BudgetFragment.this.getActivity(), createBudget.class);
        startActivity(intent);
    }

    public void saveResetDate() {
        long milliseconds = lastResetDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("lastReset", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("lastResetDate", milliseconds);
    }

    public long loadResetDate() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("lastReset", getContext().MODE_PRIVATE);
        long milliseconds = sharedPref.getLong("lastResetDate", 0);
        return milliseconds;
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
        Log.d("BTAG", "json payload: " + json);
        Log.d("BTAG", "gson from json: " + gson.fromJson(json, Budget.class));
        return gson.fromJson(json, Budget.class);
    }

    public void saveLedger() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(LEDGER_PREFS, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(budgetLedgerList);
        editor.putString(NAME_OF_LED, json);
        editor.apply();
    }

    public ArrayList<budgetDateAndTime> getLedger() {
        getContext();
        SharedPreferences sharedPref = getActivity().getSharedPreferences(LEDGER_PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(NAME_OF_LED, null);
        Log.d("BTAG", "json in getledger: " + json);
        Type type = new TypeToken<ArrayList<budgetDateAndTime>>() {}.getType();
        ArrayList<budgetDateAndTime> theList = gson.fromJson(json, type);
        return theList;
    }

    public static void addToLedger(budgetDateAndTime date) {
        budgetLedgerList.add(date);
    }

    public void goToEditBudget() {
        Log.d("BTAG", "Go to edit budget");
        Intent intent = new Intent(BudgetFragment.this.getActivity(), editBudget.class);
        startActivity(intent);
    }

    public void goToLedger() {
        Log.d("BTAG", "Go to ledger");
        Intent intent = new Intent(BudgetFragment.this.getActivity(), budgetLedger.class);
        startActivity(intent);
    }

}
