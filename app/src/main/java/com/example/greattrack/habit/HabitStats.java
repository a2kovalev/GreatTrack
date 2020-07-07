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

import org.w3c.dom.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        title.setTextColor(Color.parseColor("#FFFFFF"));
        titleCardView.setBackgroundColor(Color.parseColor("#0077ff"));
        titleCardView.setCardElevation(0);

        LinearLayout linearLayout = findViewById(R.id.StatHabitLinearLayout);


        //daily calculation
        Log.d("TAG", "calculation stuff");

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int timesToday = 0;

        for (HabitDateAndTime dateAndTime : habit.getHabitLog()) {
            if (dateAndTime.getDay() == day && (dateAndTime.getMonth() - 1) == month && dateAndTime.getYear() == year) {
                ++timesToday;
            }
        }

        List<HabitDateAndTime> habitLog = habit.getHabitLog();
        List<justHabitDate> habitDates = new ArrayList<justHabitDate>();
        for (HabitDateAndTime dateTime : habitLog) {
            Log.d("TAG", "adding " + dateTime.getDay() + "/" + dateTime.getMonth() + "/"
                    + dateTime.getYear()  + " to justHabitDate list");
            justHabitDate temp = new justHabitDate(dateTime.getDay(), dateTime.getMonth(), dateTime.getYear());
            habitDates.add(temp);
        }

        Calendar weekStart = Calendar.getInstance();
        weekStart.set(Calendar.HOUR_OF_DAY, 0);
        weekStart.clear(Calendar.MINUTE);
        weekStart.clear(Calendar.SECOND);
        weekStart.clear(Calendar.MILLISECOND);
        weekStart.set(Calendar.DAY_OF_WEEK, weekStart.getFirstDayOfWeek());
        Log.d("TAG", "start of week: " + weekStart.getTime());

        Calendar currTime = calendar.getInstance();
        Log.d("TAG", "current day: " + currTime.getTime());

        //weekly calculation
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
            tempDate.set(Calendar.MONTH, (dateAndTime.getMonth() - 1));
            tempDate.set(Calendar.YEAR, dateAndTime.getYear());
            Log.d("TAG", "tempDate time: " + tempDate.getTime());

            boolean inRange = tempDate.after(weekStart) && tempDate.before(currTime);
            Log.d("TAG", "log time is in range: " + inRange);

            if (inRange == true) {
                ++timesThisWeek;
            }
        }
        Log.d("TAG", "Times completed this week: " + timesThisWeek);

        //monthly calculation
        int timesThisMonth = 0;
        Calendar cal = Calendar.getInstance();
        int currMonth = cal.get(Calendar.MONTH) + 1;
        Log.d("TAG", "current month " + currMonth);
        for(HabitDateAndTime dateAndTime : habitLog) {
            if (dateAndTime.getMonth() == currMonth) {
                ++timesThisMonth;
            }
        }
        Log.d("TAG", "Times completed this month: " + timesThisMonth);

        //yearly calculation
        int timesThisYear = 0;
        int currYear = cal.get(Calendar.YEAR);
        Log.d("TAG", "current year " + currYear);
        for(HabitDateAndTime dateAndTime : habitLog) {
            if (dateAndTime.getYear() == currYear) {
                ++timesThisYear;
            }
        }
        Log.d("TAG", "Times completed this year: " + timesThisYear);

        HashMap<justHabitDate, Integer> timesDoneOnDaysMap = new HashMap<justHabitDate, Integer>();
        ArrayList<justHabitDate> onlyTheDates = new ArrayList<>();
        HashSet<justHabitDate> justDateSet = new HashSet<>();

        for (HabitDateAndTime dateAndTime : habitLog) {
            justHabitDate justDate = new justHabitDate(dateAndTime.getDay(), dateAndTime.getMonth(), dateAndTime.getYear());
            onlyTheDates.add(justDate);
            justDateSet.add(justDate);
        }
        Log.d("TAG", "sizes of the list and the set: " + onlyTheDates.size() + ", " + justDateSet.size());

        for (justHabitDate justDate : justDateSet) {
            timesDoneOnDaysMap.put(justDate, 0);
        }

        for (justHabitDate justDate : onlyTheDates) {
            timesDoneOnDaysMap.put(justDate, timesDoneOnDaysMap.get(justDate) + 1);
        }
        Log.d("TAG", "TimesDoneOnDays map: " + timesDoneOnDaysMap);

        //completion calculation and adding to stats
        HabitDateAndTime startLog = habitLog.get(0);
        LocalDate newBoi = LocalDate.now();

        LocalDate oldBoi = LocalDate.of(startLog.getYear(), startLog.getMonth(), startLog.getDay());

        Log.d("TAG", "oldBoi year, month, day: " + oldBoi.getYear() + ", " +
                oldBoi.getMonth() + ", " + oldBoi.getDayOfMonth());
        Log.d("TAG", "current (newBoi) date: " + newBoi.getYear() + ", " + newBoi.getMonth() + ", " + newBoi.getDayOfMonth());


        if(habit.getFreq() == HabitFrequency.daily) {
            //find days between oldBoi and newBoi and check which ones have completed habit
            HashSet<LocalDate> intervalDates = new HashSet<LocalDate>();
            HashSet<LocalDate> datesWhenHabitIsDone = new HashSet<LocalDate>();
            for (LocalDate date = oldBoi; date.isBefore(newBoi.plusDays(1)); date = date.plusDays(1)) {
                intervalDates.add(date);
            }
            for (HabitDateAndTime dateAndTime : habitLog) {
                LocalDate temp = LocalDate.of(dateAndTime.getYear(), dateAndTime.getMonth(), dateAndTime.getDay());
                justHabitDate justTemp = new justHabitDate(dateAndTime.getDay(), dateAndTime.getMonth(), dateAndTime.getYear());
                if (timesDoneOnDaysMap.get(justTemp) >= habit.getTimesDuringFreq()) {
                    datesWhenHabitIsDone.add(temp);
                }
            }
            Log.d("TAG", "total dates in interval: " + intervalDates.size());
            Log.d("TAG", "dates when habit is done: " + datesWhenHabitIsDone.size());

            double habitDoneSize = (double) datesWhenHabitIsDone.size();
            double intervalSize = (double) intervalDates.size();

            double habitCompletion = (habitDoneSize/intervalSize) * 100;
            int habitCompletePercentage = (int) Math.round(habitCompletion);
            Log.d("TAG", "habit completion: " + habitCompletePercentage);

            CardView completionCardView = new CardView(this);
            RelativeLayout.LayoutParams completionCardViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout completionCardViewRelativeLayout = new RelativeLayout(this);
            TextView textView = new TextView(this);
            textView.setText("You complete your habit ");
            RelativeLayout.LayoutParams textViewLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            textView.setId(View.generateViewId());
            TextView percentTextView = new TextView(this);
            RelativeLayout.LayoutParams percentTextViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            percentTextViewParams.setMargins(10, 0, 10, 0);
            percentTextView.setTextColor(Color.parseColor("#FFFFFF"));
            percentTextView.setTextSize((float) 35.0);
            textViewLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
            percentTextViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            percentTextViewParams.addRule(RelativeLayout.BELOW, textView.getId());
            percentTextView.setText(habitCompletePercentage + "%");
            TextView ofTheTimeView = new TextView(this);
            RelativeLayout.LayoutParams ofTheTimeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            ofTheTimeParams.setMargins(10, 0, 10, 0);
            ofTheTimeView.setTextColor(Color.parseColor("#FFFFFF"));
            ofTheTimeView.setTextSize((float) 20.0);
            ofTheTimeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            percentTextView.setId(View.generateViewId());
            ofTheTimeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            ofTheTimeParams.addRule(RelativeLayout.BELOW, percentTextView.getId());
            ofTheTimeView.setText("of the time");
            textViewLayout.setMargins(10, 0, 10, 0);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            completionCardView.setCardBackgroundColor(Color.parseColor("#0077ff"));
            completionCardView.setRadius((float) 20.0);
            textView.setTextSize((float) 20.0);
            completionCardViewParams.setMargins(10, 10, 10, 10);
            completionCardViewRelativeLayout.addView(textView, textViewLayout);
            completionCardViewRelativeLayout.addView(percentTextView, percentTextViewParams);
            completionCardViewRelativeLayout.addView(ofTheTimeView, ofTheTimeParams);
            completionCardView.addView(completionCardViewRelativeLayout);
            linearLayout.addView(completionCardView, 1, completionCardViewParams);
        }

        int iterationNum = 0;
        int numWeeks = 0;
        int weeksComplete = 0;
        oldBoi = LocalDate.of(startLog.getYear(), startLog.getMonth(), startLog.getDay());
        LocalDate today = LocalDate.now();
        LocalDate startOfFirstWeek = oldBoi.with(DayOfWeek.MONDAY);
        Log.d("TAG", "Start of the first week of habit: " + startOfFirstWeek);

        if (habit.getFreq() == HabitFrequency.weekly) {
            for (LocalDate date = startOfFirstWeek; date.isBefore(today.plusDays(1)); date = date.plusWeeks(1)) {
                LocalDate endOfWeek = date.plusDays(6);
                int timesThatWeek = 0;
                for(LocalDate dailyDate = date; dailyDate.isBefore(endOfWeek.plusDays(1)); dailyDate = dailyDate.plusDays(1)) {
                    justHabitDate checkThisDate = new justHabitDate(dailyDate.getDayOfMonth(),
                            dailyDate.getMonthValue(), dailyDate.getYear());
                    //Log.d("TAG", "date to check: " + checkThisDate.getMonth() + "/" + checkThisDate.getDay());
                    if (onlyTheDates.contains(checkThisDate)) {
                        ++timesThatWeek;
                    }
                }
                if (timesThatWeek == habit.getTimesDuringFreq()) {
                    ++weeksComplete;
                }
            }
        }

        Log.d("TAG", "weeks complete: " + weeksComplete);

        //NOW IMPLEMENT WEEKLY, MONTHLY, AND YEARLY STUFF

        //Display stuff

        //Goal Completed stuff
        if((habit.getFreq() == HabitFrequency.daily && timesToday >= habit.getTimesDuringFreq())
                || (habit.getFreq() == HabitFrequency.weekly && timesThisWeek >= habit.getTimesDuringFreq())
                || (habit.getFreq() == HabitFrequency.monthly && timesThisMonth >= habit.getTimesDuringFreq())
                || (habit.getFreq() == HabitFrequency.yearly && timesThisYear >= habit.getTimesDuringFreq())) {
                CardView dailyGoalCardView = new CardView(this);
                RelativeLayout rl = new RelativeLayout(this);
                RelativeLayout.LayoutParams dailyGoalCardParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView goalReached = new TextView(this);
                RelativeLayout.LayoutParams goalReachedParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if(habit.getFreq() == HabitFrequency.daily) {
                    goalReached.setText("DAILY GOAL REACHED");
                }
                if (habit.getFreq() == HabitFrequency.weekly) {
                    goalReached.setText("WEEKLY GOAL REACHED");
                }
                if(habit.getFreq() == HabitFrequency.monthly) {
                    goalReached.setText("MONTH GOAL REACHED");
                }
                if(habit.getFreq() == HabitFrequency.yearly) {
                    goalReached.setText("YEARLY GOAL REACHED");
                }
                goalReached.setId(View.generateViewId());

                TextView goodJob = new TextView(this);
                goodJob.setText("Great Job!");
                goodJob.setTextSize((float) 22.0);
                goodJob.setTextColor(Color.parseColor("#ffffff"));
                RelativeLayout.LayoutParams goodJobParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                goodJobParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                goodJobParams.addRule(RelativeLayout.BELOW, goalReached.getId());

                goalReachedParams.setMargins(10, 10, 10, 10);
                goalReached.setTextSize((float) 25.0);
                goalReached.setTextColor(Color.parseColor("#ffffff"));
                dailyGoalCardView.setRadius((float) 20.0);
                dailyGoalCardParams.setMargins(20, 10, 20, 10);
                goalReachedParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl.addView(goalReached, goalReachedParams);
                rl.addView(goodJob, goodJobParams);
                dailyGoalCardView.addView(rl);
                dailyGoalCardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                dailyGoalCardParams.setMargins(10, 10, 10, 10);
                linearLayout.addView(dailyGoalCardView, 1, dailyGoalCardParams);
        }


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
            case monthly:
                freqLabel = " " + timeOrTimes + " this month";
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
        RelativeLayout.LayoutParams thisWeekTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        thisWeekTextParams.setMargins(10, 0, 0, 0);
        thisWeekCardParams.setMargins(20, 10, 20 ,10);
        thisWeekTextView.setText("You have done this " + timesThisWeek + " times this week!");
        thisWeekTextView.setTextSize((float) 20.0);
        thisWeekTextView.setTextColor(Color.parseColor("#fafafa"));
        timesThisWeekCardView.addView(thisWeekTextView, thisWeekTextParams);
        linearLayout.addView(timesThisWeekCardView, thisWeekCardParams);

        //being times this month card
        CardView timesThisMonthCardView = new CardView(this);
        RelativeLayout.LayoutParams thisMonthCardParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        timesThisMonthCardView.setRadius((float) 20.0);
        timesThisMonthCardView.setCardBackgroundColor(Color.parseColor("#10cc3f"));
        TextView thisMonthTextView = new TextView(this);
        RelativeLayout.LayoutParams thisMonthTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        thisMonthTextParams.setMargins(10, 0, 0, 0);
        thisMonthCardParams.setMargins(20, 10, 20 ,10);
        thisMonthTextView.setText("This month, you have done this " + timesThisMonth + " times!");
        thisMonthTextView.setTextSize((float) 20.0);
        thisMonthTextView.setTextColor(Color.parseColor("#fafafa"));
        timesThisMonthCardView.addView(thisMonthTextView, thisMonthTextParams);
        linearLayout.addView(timesThisMonthCardView, thisMonthCardParams);

        //being times this year card
        CardView timesThisYearCardView = new CardView(this);
        RelativeLayout.LayoutParams thisYearCardParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        timesThisYearCardView.setRadius((float) 20.0);
        timesThisYearCardView.setCardBackgroundColor(Color.parseColor("#10cc3f"));
        TextView thisYearTextView = new TextView(this);
        RelativeLayout.LayoutParams thisYearTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        thisYearTextParams.setMargins(10, 0, 0, 0);
        thisYearCardParams.setMargins(20, 10, 20 ,10);
        thisYearTextView.setText("So far in " + currYear + ", you have done this " + timesThisYear + " times!");
        thisYearTextView.setTextSize((float) 20.0);
        thisYearTextView.setTextColor(Color.parseColor("#fafafa"));
        timesThisYearCardView.addView(thisYearTextView, thisYearTextParams);
        linearLayout.addView(timesThisYearCardView, thisYearCardParams);
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

        @Override
        public int hashCode() {
            return Objects.hash(day, month, year);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if(!(obj instanceof justHabitDate)) {
                return false;
            }

            justHabitDate justDate = (justHabitDate) obj;

            return (this.day == justDate.day) && (this.month == justDate.month) && (this.year == justDate.year);
        }
    }
}
