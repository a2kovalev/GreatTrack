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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        //Calculation stuff
        Log.d("TAG", "calculation stuff");
        List<HabitDateAndTime> habitLog = habit.getHabitLog();
        List<justHabitDate> habitDates = new ArrayList<justHabitDate>();
        for (HabitDateAndTime dateTime : habitLog) {
            Log.d("TAG", "adding " + dateTime.getDay() + "/" + dateTime.getMonth() + "/"
                    + dateTime.getYear()  + " to justHabitDate list");
            justHabitDate temp = new justHabitDate(dateTime.getDay(), dateTime.getMonth(), dateTime.getYear());
            habitDates.add(temp);
        }
        Map<justHabitDate, Integer> dateFreqMap = new HashMap<justHabitDate, Integer>();

        Calendar weekStart = Calendar.getInstance();
        weekStart.set(Calendar.HOUR_OF_DAY, 0);
        weekStart.clear(Calendar.MINUTE);
        weekStart.clear(Calendar.SECOND);
        weekStart.clear(Calendar.MILLISECOND);
        weekStart.set(Calendar.DAY_OF_WEEK, weekStart.getFirstDayOfWeek());
        Log.d("TAG", "start of week: " + weekStart.getTime());

        Calendar currTime = calendar.getInstance();
        Log.d("TAG", "current day: " + currTime.getTime());

        int timesThisWeek = 0;
        for(HabitDateAndTime dateAndTime : habitLog) {
            Calendar tempDate = Calendar.getInstance();
            tempDate.clear(Calendar.HOUR_OF_DAY);
            tempDate.clear(Calendar.MINUTE);
            tempDate.clear(Calendar.SECOND);
            tempDate.clear(Calendar.MILLISECOND);
            tempDate.set(Calendar.HOUR_OF_DAY, dateAndTime.getHour());
            tempDate.set(Calendar.MINUTE, dateAndTime.getMinute());
            tempDate.set(Calendar.DAY_OF_MONTH, dateAndTime.getDay());
            tempDate.set(Calendar.MONTH, dateAndTime.getMonth());
            tempDate.set(Calendar.YEAR, dateAndTime.getYear());
            Log.d("TAG", "tempDate time: " + tempDate.getTime());

            boolean inRange = tempDate.after(weekStart) && tempDate.before(currTime);
            Log.d("TAG", "log time is in range: " + inRange);

            if (inRange == true) {
                ++timesThisWeek;
            }
        }
        Log.d("TAG", "Times completed this week: " + timesThisWeek);

        //Display stuff
        //begin times today card
        CardView timesTodayCardView = new CardView(this);
        timesTodayCardView.setRadius((float) 20.0);
        timesTodayCardView.setCardBackgroundColor(Color.parseColor("#10cc3f"));
        RelativeLayout.LayoutParams timesTodayLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout InsideTimesTodayLayout = new RelativeLayout(this);
        TextView currDay = new TextView(this);
        RelativeLayout.LayoutParams currDayLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        currDayLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        currDayLayoutParams.setMargins(10, 0, 0, 0);
        currDay.setTextSize((float) 20.0);
        currDay.setTextColor(Color.parseColor("#fafafa"));
        if (timesToday != 1) {
            currDay.setText("Completed " + String.valueOf(timesToday) + " times today!");
        } else {
            currDay.setText("Completed " + String.valueOf(timesToday) + " time today!");
        }

        TextView goalText = new TextView(this);
        String freqLabel = "";
        String timeOrTimes = "";
        if (habit.getTimesDuringFreq() == 1) {
            timeOrTimes = "time";
        } else {
            timeOrTimes = "times";
        }

        switch (habit.getFreq()) {
            case daily:
                freqLabel = " " + timeOrTimes + " today";
                break;
            case weekly:
                freqLabel = " " + timeOrTimes + " this week";
                break;
            case biweekly:
                freqLabel = " " + timeOrTimes + " this fortnight";
                break;
            case monthly:
                freqLabel = " " + timeOrTimes + " this month";
            case halfYearly:
                freqLabel = " " + timeOrTimes + " this half-year";
            case yearly:
                freqLabel = " " + timeOrTimes + " this year";
        }

        currDay.setId(View.generateViewId());
        goalText.setText("Goal: " + habit.timesDuringFreq + freqLabel);
        goalText.setTextSize((float) 17.0);
        goalText.setTextColor(Color.parseColor("#fafafa"));
        RelativeLayout.LayoutParams goalTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        goalTextParams.addRule(RelativeLayout.BELOW, currDay.getId());
        goalTextParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        goalTextParams.setMargins(10, 0, 0, 0);
        InsideTimesTodayLayout.addView(currDay, currDayLayoutParams);
        InsideTimesTodayLayout.addView(goalText, goalTextParams);
        timesTodayCardView.addView(InsideTimesTodayLayout);

        timesTodayLayoutParams.setMargins(20, 10, 20 ,10);

        linearLayout.addView(timesTodayCardView, timesTodayLayoutParams);
        //end times today card

        //begin times this week card
        CardView timesThisWeekCardView = new CardView(this);
        RelativeLayout.LayoutParams thisWeekCardParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        timesThisWeekCardView.setRadius((float) 20.0);
        timesThisWeekCardView.setCardBackgroundColor(Color.parseColor("#10cc3f"));
        TextView thisWeekTextView = new TextView(this);
        thisWeekTextView.setText("You have done this " + timesThisWeek + " times this week!");
        timesThisWeekCardView.addView(thisWeekTextView);
        linearLayout.addView(timesThisWeekCardView, thisWeekCardParams);
    }

    private class justHabitDate {
        int day;
        int month;
        int year;

        private justHabitDate(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        private int getDay() {
            return day;
        }

        private int getMonth() {
            return month;
        }

        private int getYear() {
            return year;
        }
    }
}
