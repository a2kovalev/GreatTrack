package com.example.greattrack.habit;

import java.io.Serializable;

enum HabitFrequency {
    halfHourly, hourly, daily, weekly, biweekly, monthly, halfYearly, yearly
}

public class Habit implements Serializable {
    String habitName;
    int timesDuringFreq;
    boolean remindersOn;
    HabitFrequency freq;

    public Habit(String habitName, int timesDuringFreq, boolean remindersOn, HabitFrequency freq) {
        this.habitName = habitName;
        this.timesDuringFreq = timesDuringFreq;
        this.remindersOn = remindersOn;
        this.freq = freq;
    }

    public String getHabitName() {
        return habitName;
    }

    public int getTimesDuringFreq() {
        return timesDuringFreq;
    }

    public boolean isRemindersOn() {
        return remindersOn;
    }

    public HabitFrequency getFreq() {
        return freq;
    }
}
