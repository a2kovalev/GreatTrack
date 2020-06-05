package com.example.greattrack;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HabitFragment extends Fragment {
    private static final String TAG = "Habit Fragment";
    public int habitListSize = HabitDialogueFragment.getHabitListSize();
    private LinearLayout linearLayout = null;
    private List<Habit> habitList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "HabitFragment OnCreateView");
        Log.d("TAG", "habit list size before add new habit: " + habitListSize);
        loadData();
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        Button newHabitButton = view.findViewById(R.id.newHabitButton);
        linearLayout = view.findViewById(R.id.linearLayoutInHabitScrollView);
        newHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDialogue();
            }
        });

        Log.d("TAG", "return view");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        habitList = HabitDialogueFragment.getHabitList();
        loadData();
        createCards();
    }

    public void createCards() {
        Log.d("TAG", "createCards method");
        habitList = HabitDialogueFragment.getHabitList();
        if (HabitDialogueFragment.getHabitListSize() > 0) {
                for(int i = 0; i < linearLayout.getChildCount(); i++) {
                    View temp = linearLayout.getChildAt(i);
                    if (temp instanceof MyCardView) {
                        linearLayout.removeView(temp);
                    }
                }
                for (Habit newHabit: habitList) {
                    Log.d("TAG", "adding to linearLayout, am in if statement");
                    habitListSize = HabitDialogueFragment.getHabitListSize();
                    MyCardView cardView = new MyCardView(this.getContext());
                    cardView.setId(View.generateViewId());
                    CardView.LayoutParams cardLayoutParams = new CardView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
                    cardLayoutParams.setMargins(20, 10, 20, 10);
                    cardView.setLayoutParams(cardLayoutParams);
                    //cardView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
                    cardView.setRadius((float) 20.0);
                    cardView.setBackgroundColor(Color.parseColor("#ffa500"));
                    TextView freqText = new TextView(this.getContext());
                    freqText.setTextSize((float) 15.0);

                    String freqLabel = "";

                    switch (newHabit.freq) {
                        case hourly:
                            freqLabel = "hour";
                            break;
                        case halfHourly:
                            freqLabel = "30 minutes";
                            break;
                        case daily:
                            freqLabel = "day";
                            break;
                        case weekly:
                            freqLabel = "week";
                            break;
                        case biweekly:
                            freqLabel = "2 weeks";
                            break;
                        case monthly:
                            freqLabel = "month";
                            break;
                        case halfYearly:
                            freqLabel = "6 months";
                            break;
                        case yearly:
                            freqLabel = "year";
                            break;
                    }

                    freqText.setText(newHabit.timesDuringFreq + " times every " + freqLabel);
                    freqText.setId(View.generateViewId());

                    RelativeLayout rl = new RelativeLayout(this.getContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    TextView text = new TextView(this.getContext());
                    text.setTextColor(Color.parseColor("#ffffff"));
                    text.setText(newHabit.habitName);
                    text.setTextSize((float) 19.0);
                    text.setId(View.generateViewId());
                    layoutParams.setMargins(10, 0, 0, 0);
                    layoutParams2.setMargins(10, 0, 0, 0);

                    rl.addView(text, layoutParams);
                    layoutParams2.addRule(RelativeLayout.BELOW, text.getId());
                    rl.addView(freqText, layoutParams2);

                    cardView.addView(rl);

                    linearLayout.addView(cardView);
                    saveData();
                }
        } else {
            Log.d("TAG", "habit list size not changed");
        }
    }

    private void goToDialogue() {
        Log.d("TAG", "HabitFragment goToDialogue");
        Intent intent = new Intent(HabitFragment.this.getActivity(), HabitDialogueFragment.class);
        startActivity(intent);
    }

    public void saveData() {
        Log.d("TAG", "saveData method");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(habitList);
        editor.putString("habit list", json);
        editor.commit();
    }

    public void loadData() {
        Log.d("TAG", "loadData method");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("habit list", null);
        Type type = new TypeToken<ArrayList<Habit>>() {}.getType();
        habitList = gson.fromJson(json, type);

        if(habitList == null) {
            habitList = new ArrayList<>();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }
}
