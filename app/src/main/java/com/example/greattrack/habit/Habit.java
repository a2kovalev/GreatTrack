package com.example.greattrack.habit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

enum HabitFrequency {
    daily, weekly, biweekly, monthly, halfYearly, yearly
}

public class Habit implements Serializable {
    String habitName;
    int timesDuringFreq;
    boolean remindersOn;
    HabitFrequency freq;
    ArrayList<HabitDateAndTime> HabitLog = new ArrayList<HabitDateAndTime>();

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

    public void setDatesAndTimes(ArrayList<HabitDateAndTime> datesAndTimes) {
        this.HabitLog = datesAndTimes;
    }

    public void addToLog(HabitDateAndTime dateAndTime) {
        HabitLog.add(dateAndTime);
    }

    public void removeFromLog(HabitDateAndTime dateAndTime) {
        HabitLog.remove(dateAndTime);
    }

    public List<HabitDateAndTime> getHabitLog () {
        return HabitLog;
    }
}
