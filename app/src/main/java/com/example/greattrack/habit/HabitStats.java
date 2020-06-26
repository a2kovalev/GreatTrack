package com.example.greattrack.habit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.greattrack.R;

import java.util.Calendar;

public class HabitStats extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "Habit stats activity");
        setContentView(R.layout.habit_stats);
        getSupportActionBar().setTitle("Habit Statistics");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10cc3f")));
        CardView titleCardView = findViewById(R.id.StatHabitTitleCardView);
        titleCardView.setBackgroundColor(Color.parseColor("#ffffff"));
        TextView title = findViewById(R.id.StatHabitName);
        title.setGravity(Gravity.CENTER);
        Intent intent = getIntent();
        Habit habit = (Habit) intent.getSerializableExtra("StatSentHabit");
        title.setText("Stats for " + "\"" + habit.habitName + "\"");

        LinearLayout linearLayout = findViewById(R.id.StatHabitLinearLayout);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int timesToday = 0;

        for (HabitDateAndTime dateAndTime : habit.getHabitLog()) {
            if (dateAndTime.getDay() == day && dateAndTime.getMonth() == month && dateAndTime.getYear() == year) {
                ++timesToday;
            }
        }

        CardView timesTodayCardView = new CardView(this);
        RelativeLayout.LayoutParams timesTodayLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout InsideTimesTodayLayout = new RelativeLayout(this);
        TextView currDay = new TextView(this);
        RelativeLayout.LayoutParams currDayLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        currDayLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        currDay.setTextSize((float) 20.0);
        currDay.setTextColor(Color.parseColor("#f78000"));
        if (timesToday != 1) {
            currDay.setText("Completed " + String.valueOf(timesToday) + " times today!");
        } else {
            currDay.setText("Completed " + String.valueOf(timesToday) + " time today!");
        }

        TextView goalText = new TextView(this);
        String freqLabel = "";
        switch (habit.getFreq()) {
            case daily:
                freqLabel = " times today";
                break;
            case weekly:
                freqLabel = " times this week";
                break;
            case biweekly:
                freqLabel = " times this fortnight";
                break;
            case monthly:
                freqLabel = " times this month";
            case halfYearly:
                freqLabel = " times this half-year";
            case yearly:
                freqLabel = " times this year";
        }

        currDay.setId(View.generateViewId());
        goalText.setText("Goal: " + habit.timesDuringFreq + freqLabel);
        goalText.setTextSize((float) 17.0);
        RelativeLayout.LayoutParams goalTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        goalTextParams.addRule(RelativeLayout.BELOW, currDay.getId());
        goalTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        InsideTimesTodayLayout.addView(currDay, currDayLayoutParams);
        InsideTimesTodayLayout.addView(goalText, goalTextParams);
        timesTodayCardView.addView(InsideTimesTodayLayout);
        timesTodayCardView.setRadius((float) 20.0);

        timesTodayLayoutParams.setMargins(10, 10, 10 ,10);

        linearLayout.addView(timesTodayCardView, timesTodayLayoutParams);

    }
}
