package com.example.greattrack;


import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class HabitFragment extends Fragment {
    private static final String TAG = "Habit Fragment";
    public int habitListSize = HabitDialogueFragment.getHabitListSize();
    private LinearLayout linearLayout = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "HabitFragment OnCreateView");
        Log.d("TAG", "habit list size before add new habit: " + habitListSize);
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
        if (HabitDialogueFragment.getHabitListSize() > habitListSize) {
            Log.d("TAG", "adding to linearLayout, am in if statement");
            habitListSize = HabitDialogueFragment.getHabitListSize();
            Habit newHabit = HabitDialogueFragment.habitList.get(habitListSize - 1);
            MyCardView cardView = new MyCardView(this.getContext());
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
                case halfHourly:
                    freqLabel = "30 minutes";
                case daily:
                    freqLabel = "day";
                case weekly:
                    freqLabel = "week";
                case biweekly:
                    freqLabel = "2 weeks";
                case monthly:
                    freqLabel = "month";
                case halfYearly:
                    freqLabel = "6 months";
                case yearly:
                    freqLabel = "year";
            }

            freqText.setText(newHabit.timesDuringFreq + " times every " + freqLabel);
            freqText.setId(View.generateViewId());

            RelativeLayout rl = new RelativeLayout(this.getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextView text = new TextView(this.getContext());
            text.setTextColor(Color.parseColor("#ffffff"));
            text.setText(newHabit.habitName);
            text.setTextSize((float)19.0);
            text.setId(View.generateViewId());
            layoutParams.setMargins(10, 0,0,0);
            layoutParams2.setMargins(10,0,0,0);

            rl.addView(text, layoutParams);
            layoutParams2.addRule(RelativeLayout.BELOW, text.getId());
            rl.addView(freqText, layoutParams2);

            cardView.addView(rl);

            linearLayout.addView(cardView);
        } else {
            Log.d("TAG", "habit list size not changed");
        }
    }

    private void goToDialogue() {
        Log.d("TAG", "HabitFragment goToDialogue");
        Intent intent = new Intent(HabitFragment.this.getActivity(), HabitDialogueFragment.class);
        startActivity(intent);
    }

}
