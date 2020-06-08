package com.example.greattrack.habit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greattrack.R;

import java.util.ArrayList;
import java.util.List;

public class HabitDialogueActivity extends AppCompatActivity {
    public static final String TAG = "Habit Dialogue Fragment Activity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("TAG", "In OnCreate of HabitDialogue");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialogue_habit);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10cc3f")));
        getSupportActionBar().setTitle("Add a New Habit");
        Spinner spinner = (Spinner) findViewById(R.id.freqSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button createHabitButton = findViewById(R.id.createHabitButton);

        createHabitButton.setOnClickListener((v) -> {
            EditText habitNameTextField = findViewById(R.id.habitName);
            EditText frequencyTextField = findViewById(R.id.habitFrequency);
            Switch remindersOnSwitch = findViewById(R.id.reminderSwitch);
            Spinner durationSpinner = findViewById(R.id.freqSpinner);
            String freqText = frequencyTextField.getText().toString();
            String habitName = habitNameTextField.getText().toString();
            String durationText = durationSpinner.getSelectedItem().toString();
            int frequency = Integer.parseInt(freqText);
            boolean remindersOn = remindersOnSwitch.isChecked();

            HabitFrequency duration;
            if (durationText.equals("Half Hour")) {
                duration = HabitFrequency.halfHourly;
            } else if (durationText.equals("Hour")) {
                duration = HabitFrequency.hourly;
            } else if (durationText.equals("Day")) {
                duration = HabitFrequency.daily;
            } else if (durationText.equals("Week")) {
                duration = HabitFrequency.weekly;
            } else if (durationText.equals("2 Weeks")) {
                duration = HabitFrequency.biweekly;
            } else if (durationText.equals("Month")) {
                duration = HabitFrequency.monthly;
            } else if (durationText.equals("6 Months")) {
                duration = HabitFrequency.halfYearly;
            } else {
                duration = HabitFrequency.yearly;
            }

            Log.d("TAG", "Clicked add habit button");
            Habit newHabit = new Habit(habitName, frequency, remindersOn, duration);
            HabitFragment.habitList.add(newHabit);
            finish();});
    }
}
