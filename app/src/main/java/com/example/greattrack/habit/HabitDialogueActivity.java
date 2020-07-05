package com.example.greattrack.habit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greattrack.R;

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
        createHabitButton.setTextColor(Color.parseColor("#ffffff"));

        Switch remindersOnSwitch = findViewById(R.id.reminderSwitch);
        remindersOnSwitch.setVisibility(View.INVISIBLE);

        int[] reminderTime = {-1, -1};

        Spinner durationSpinner = findViewById(R.id.freqSpinner);

      /*  durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Day") && ) {
                    remindersOnSwitch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        remindersOnSwitch.setOnClickListener((v) -> {
            if (remindersOnSwitch.isChecked()) {
                Log.d("TAG", "Reminder switch checked, building alert");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Reminder Time");
                builder.setMessage("When would you like to be reminded?");
                final View customLayout = getLayoutInflater().inflate(R.layout.habit_custom_daily_alert, null);
                builder.setView(customLayout);
                //TimePicker timePicker = (TimePicker) findViewById(R.id.dailyHabitTimePicker);
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimePicker timePicker = (TimePicker) customLayout.findViewById(R.id.dailyHabitTimePicker);
                        reminderTime[0] = timePicker.getHour();
                        reminderTime[1] = timePicker.getMinute();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remindersOnSwitch.setChecked(false);
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }); */

        createHabitButton.setOnClickListener((v) -> {
            EditText habitNameTextField = findViewById(R.id.habitName);
            EditText frequencyTextField = findViewById(R.id.habitFrequency);
            String freqText = frequencyTextField.getText().toString();
            String habitName = habitNameTextField.getText().toString();
            int frequency = Integer.parseInt(freqText);
            /*boolean remindersOn = remindersOnSwitch.isChecked(); */
            boolean remindersOn = false;
            String durationText = durationSpinner.getSelectedItem().toString();

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

            Log.d("TAG", "Clicked add habit button");
            Habit newHabit = new Habit(habitName, frequency, remindersOn, duration);
            HabitFragment.habitList.add(newHabit);

            if (reminderTime[0] != -1 && reminderTime[1] != -1) {
                HabitFragment.remindersOnHabits.put(newHabit, reminderTime);
            }

            finish();});
    }
}
