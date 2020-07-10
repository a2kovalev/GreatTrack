package com.example.greattrack.habit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greattrack.R;

import java.util.ArrayList;

public class editHabit extends AppCompatActivity {
    public static final String TAG = "Edit Habit Dialogue Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("TAG", "In OnCreate of HabitDialogue");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.edit_habit_alert);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10cc3f")));
        getSupportActionBar().setTitle("Edit a Habit");
        Spinner spinner = (Spinner) findViewById(R.id.freqSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button createHabitButton = findViewById(R.id.createHabitButton);
        createHabitButton.setText("Edit Habit");
        createHabitButton.setTextColor(Color.parseColor("#ffffff"));

        Switch remindersOnSwitch = findViewById(R.id.reminderSwitch);
        remindersOnSwitch.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        Habit habit = (Habit) intent.getSerializableExtra("HabitToEdit");
        int habitIndex = (Integer) intent.getSerializableExtra("IndexOfHabit");
        Log.d("TAG", "habit Index: " + habitIndex);


        int[] reminderTime = {-1, -1};

        Spinner durationSpinner = findViewById(R.id.freqSpinner);

        EditText habitNameTextField = findViewById(R.id.habitName);
        habitNameTextField.setText(habit.habitName);
        EditText frequencyTextField = findViewById(R.id.habitFrequency);
        String freqTimes = Integer.toString(habit.getTimesDuringFreq());
        frequencyTextField.setText(freqTimes);
        String freqText = frequencyTextField.getText().toString();
        String habitName = habitNameTextField.getText().toString();
        int frequency = Integer.parseInt(freqText);
        String habitFreqText = habit.getFreq().toString();
        if (habitFreqText.equals("daily")) {
            habitFreqText = "Day";
        } else if (habitFreqText.equals("weekly")) {
            habitFreqText = "Week";
        } else if (habitFreqText.equals("monthly")) {
            habitFreqText = "Month";
        } else {
            habitFreqText = "Year";
        }
        durationSpinner.setSelection(adapter.getPosition(habitFreqText));

        createHabitButton.setOnClickListener((v) -> {
            /*boolean remindersOn = remindersOnSwitch.isChecked(); */
            boolean remindersOn = false;
            String durationText = durationSpinner.getSelectedItem().toString();
            if (!durationText.equals("Select")) {
                HabitFrequency duration;
                if (durationText.equals("Day")) {
                    duration = HabitFrequency.daily;
                } else if (durationText.equals("Week")) {
                    duration = HabitFrequency.weekly;
                } else if (durationText.equals("Month")) {
                    duration = HabitFrequency.monthly;
                } else {
                    duration = HabitFrequency.yearly;
                }
                Log.d("TAG", "Clicked finish edit habit button");

                habit.setHabitName(habitNameTextField.getText().toString());
                habit.setFreq(duration);
                habit.setTimesDuringFreq(frequency);
                HabitFragment.habitList.set(habitIndex, habit);

                finish();
            } else {
                TextView selectWarning = new TextView(this);
                RelativeLayout.LayoutParams selectParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                selectWarning.setText("Please select duration for habit");
                selectWarning.setTextColor(Color.parseColor("#FF0000"));
                selectWarning.setTextSize((float) 16.0);
                selectParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                selectParams.addRule(RelativeLayout.ABOVE, createHabitButton.getId());
                RelativeLayout relativeLayout = findViewById(R.id.dialogueRelativeLayout);
                relativeLayout.addView(selectWarning, selectParams);
            }
        });
    }
}
