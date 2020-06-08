package com.example.greattrack.habit;


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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.greattrack.MyCardView;
import com.example.greattrack.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HabitFragment extends Fragment {
    private static final String TAG = "Habit Fragment";
    private LinearLayout linearLayout = null;
    public static List<Habit> habitList = new ArrayList<Habit>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "HabitFragment OnCreateView");
        Log.d("TAG", "habit list size before add new habit: " + habitList.size());
        habitList = getHabitList();
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
        saveHabitList();
        createCards();
    }

    public void createCards() {
        Log.d("TAG", "createCards method");
        if (habitList.size() > 0) {
                for(int i = 0; i < linearLayout.getChildCount(); i++) {
                    View temp = linearLayout.getChildAt(i);
                    if (temp instanceof MyCardView) {
                        linearLayout.removeView(temp);
                    }
                }
                for (Habit newHabit: habitList) {
                    Log.d("TAG", "adding to linearLayout, am in if statement");
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
                    String mightBePlural = " times every ";
                    if (newHabit.timesDuringFreq == 1) {
                        mightBePlural = " time every ";
                    }

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

                    freqText.setText(newHabit.timesDuringFreq + mightBePlural + freqLabel);
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
                }
        } else {
            Log.d("TAG", "habit list size not changed");
        }
    }

    private void goToDialogue() {
        Log.d("TAG", "HabitFragment goToDialogue");
        Intent intent = new Intent(HabitFragment.this.getActivity(), HabitDialogueActivity.class);
        startActivity(intent);
    }

    public void saveHabitList(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(habitList);
        editor.clear();
        editor.putString("habit list", json);
        editor.commit();
    }

    public ArrayList<Habit> getHabitList(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("habit list", null);
        Type type = new TypeToken<ArrayList<Habit>>() {}.getType();
        return gson.fromJson(json, type);
    }


//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
}
