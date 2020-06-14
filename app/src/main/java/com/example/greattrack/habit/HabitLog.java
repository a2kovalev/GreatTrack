package com.example.greattrack.habit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greattrack.R;

import java.util.ArrayList;
import java.util.List;

public class HabitLog extends AppCompatActivity {
    List<HabitDateAndTime> datesAndTimes = new ArrayList<HabitDateAndTime>();
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
        TextView titleName = findViewById(R.id.logHabitName);
        titleName.setText(habit.habitName);
        TextView subheadingName = findViewById(R.id.logHabitFrequency);
        String freqLabel = "";
        switch (habit.freq) {
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

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TimePicker timePicker = (TimePicker) customLayout.findViewById(R.id.logTimePicker);
                    DatePicker datePicker = (DatePicker) customLayout.findViewById(R.id.logDatePicker);

                    dateTime[0] = new HabitDateAndTime(timePicker.getHour(), timePicker.getMinute(),
                            datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    datesAndTimes.add(dateTime[0]);

                    textView.setText("Done at the hour " + dateTime[0].getHour());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.addView(textView);

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
    }
}
