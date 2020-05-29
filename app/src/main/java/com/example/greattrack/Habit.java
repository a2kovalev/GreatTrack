package com.example.greattrack;

enum HabitFrequency {
    halfHourly, hourly, daily, weekly, biweekly, monthly, halfYearly, yearly
}

public class Habit {
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
