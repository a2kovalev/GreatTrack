package com.example.greattrack.habit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.greattrack.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HabitLog extends AppCompatActivity {
    ArrayList<HabitDateAndTime> datesAndTimes = new ArrayList<HabitDateAndTime>();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "Habit Log activity");
        RelativeLayout relativeLayout = findViewById(R.id.habitLogRelativeLayout);
        ScrollView scrollView = findViewById(R.id.HabitLogScrollView);
        setContentView(R.layout.habit_log);
        getSupportActionBar().setTitle("Habit Log");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10cc3f")));
        Intent intent = getIntent();
        Habit habit = (Habit) intent.getSerializableExtra("SentHabit");
        oldLogEntries(habit);
        Button doneButton = findViewById(R.id.LogDoneButton);
        int index = HabitFragment.habitList.indexOf(habit);
        Log.d("TAG", "Habit index is: " + index);
        if (habit.getHabitLog() != null) {
            datesAndTimes = habit.HabitLog;
        }
        TextView titleName = findViewById(R.id.logHabitName);
        titleName.setText(habit.habitName);
        TextView subheadingName = findViewById(R.id.logHabitFrequency);
        String freqLabel = "";
        switch (habit.freq) {
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

        if (habit.timesDuringFreq != 1) {
            subheadingName.setText(habit.timesDuringFreq + " times every " + freqLabel);
        } else {
            subheadingName.setText(habit.timesDuringFreq + " time every " + freqLabel);
        }

        final HabitDateAndTime[] dateTime = new HabitDateAndTime[1];

        Button logButton = findViewById(R.id.LogTheHabitButton);
        logButton.setOnClickListener(v-> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Log Habit");
            builder.setMessage("When did you perform this habit?");
            final View customLayout = getLayoutInflater().inflate(R.layout.alert_log_habit, null);
            builder.setView(customLayout);

            TextView textView = new TextView(this);
            LinearLayout linearLayout = findViewById(R.id.HabitLogLinearLayout);
            CardView cardView = new CardView(this);

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TimePicker timePicker = (TimePicker) customLayout.findViewById(R.id.logTimePicker);
                    DatePicker datePicker = (DatePicker) customLayout.findViewById(R.id.logDatePicker);

                    dateTime[0] = new HabitDateAndTime(timePicker.getHour(), timePicker.getMinute(),
                            datePicker.getYear(), (datePicker.getMonth() + 1), datePicker.getDayOfMonth());
                    datesAndTimes.add(dateTime[0]);
                    displayLog();
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

        doneButton.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putParcelableArrayListExtra("dateTimes", datesAndTimes);
            setResult(RESULT_OK, data);
            habit.sortHabitLog();
            finish();
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void oldLogEntries(Habit habit) {
        LinearLayout linearLayout = findViewById(R.id.HabitLogLinearLayout);
        if (habit.HabitLog != null) {
            for (HabitDateAndTime dateAndTime : habit.HabitLog) {
                TextView textView = new TextView(this);
                textView.setId(View.generateViewId());
                CardView cardView = new CardView(this);
                RelativeLayout rl = new RelativeLayout(this);
                RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                String lessThanTenTime = "";
                if (dateAndTime.getMinute() < 10) {
                    lessThanTenTime = "0" + dateAndTime.getMinute();
                    textView.setText("Done at " + dateAndTime.getHour() + ":" + lessThanTenTime + " on "
                            + dateAndTime.getMonth() + "/" + dateAndTime.getDay() + "/" + dateAndTime.getYear());
                } else {
                    textView.setText("Done at " + dateAndTime.getHour() + ":" + dateAndTime.getMinute() + " on "
                            + dateAndTime.getMonth() + "/" + dateAndTime.getDay() + "/" + dateAndTime.getYear());
                }
                textView.setTextSize((float) 20.0);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                cardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Button deleteButton = new Button(this);
                deleteButton.setText("X");
                deleteButton.setTextSize((float) 20.0);
                deleteButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                deleteButton.setStateListAnimator(null);
                deleteButton.setTextColor(Color.parseColor("#FF0000"));

                deleteButton.setOnClickListener(v -> {
                    habit.getHabitLog().remove(dateAndTime);
                    linearLayout.removeView(cardView);
                });

                RelativeLayout.LayoutParams deleteButtonParams =
                        new RelativeLayout.LayoutParams(60, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams textLayout =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams cardLayout =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                cardLayout.setMargins(10,10,10, 10);
                textLayout.addRule(RelativeLayout.CENTER_VERTICAL);
                textLayout.setMargins(5, 0, 0, 0);
                //deleteButtonParams.addRule(RelativeLayout.RIGHT_OF, textView.getId());
                deleteButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                deleteButtonParams.setMargins(10, 10, 30, 10);
                rl.addView(deleteButton, deleteButtonParams);
                rl.addView(textView, textLayout);
                cardView.addView(rl, rlParams);
                cardView.setRadius((float) 20.0);
                linearLayout.addView(cardView, 0, cardLayout);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void displayLog() {
        Log.d("TAG", "displayLog() called");
        Log.d("TAG", "datesAndTimes list size: " + datesAndTimes.size());
        LinearLayout linearLayout = findViewById(R.id.HabitLogLinearLayout);
        if (linearLayout != null) {
            linearLayout.removeAllViewsInLayout();
        }
        for (HabitDateAndTime dateAndTime : datesAndTimes) {
            TextView textView = new TextView(this);
            textView.setId(View.generateViewId());
            CardView cardView = new CardView(this);
            RelativeLayout rl = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            String lessThanTenTime = "";
            if (dateAndTime.getMinute() < 10) {
                lessThanTenTime = "0" + dateAndTime.getMinute();
                textView.setText("Done at " + dateAndTime.getHour() + ":" + lessThanTenTime + " on "
                        + dateAndTime.getMonth() + "/" + dateAndTime.getDay() + "/" + dateAndTime.getYear());
            } else {
                textView.setText("Done at " + dateAndTime.getHour() + ":" + dateAndTime.getMinute() + " on "
                        + dateAndTime.getMonth() + "/" + dateAndTime.getDay() + "/" + dateAndTime.getYear());
            }
            textView.setTextSize((float) 20.0);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            cardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            Button deleteButton = new Button(this);
            deleteButton.setText("X");
            deleteButton.setTextSize((float) 20.0);
            deleteButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
            deleteButton.setStateListAnimator(null);
            deleteButton.setTextColor(Color.parseColor("#FF0000"));

            deleteButton.setOnClickListener(v -> {
                datesAndTimes.remove(dateAndTime);
                linearLayout.removeView(cardView);
            });

            RelativeLayout.LayoutParams deleteButtonParams =
                    new RelativeLayout.LayoutParams(60, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams textLayout =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textLayout.addRule(RelativeLayout.CENTER_VERTICAL);
            RelativeLayout.LayoutParams cardLayout =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            cardLayout.setMargins(10,10,10, 10);
            textLayout.setMargins(5, 0, 0, 0);
            //deleteButtonParams.addRule(RelativeLayout.RIGHT_OF, textView.getId());
            deleteButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            deleteButtonParams.setMargins(10, 10, 30, 10);
            rl.addView(deleteButton, deleteButtonParams);
            rl.addView(textView, textLayout);
            cardView.addView(rl, rlParams);
            cardView.setRadius((float) 20.0);
            linearLayout.addView(cardView, 0, cardLayout);
        }
    }

}
