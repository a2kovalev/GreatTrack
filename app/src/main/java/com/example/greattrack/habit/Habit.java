package com.example.greattrack.habit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum HabitFrequency {
    daily, weekly, monthly, yearly
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

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public void setTimesDuringFreq(int timesDuringFreq) {
        this.timesDuringFreq = timesDuringFreq;
    }

    public void setRemindersOn(boolean remindersOn) {
        this.remindersOn = remindersOn;
    }

    public void setFreq (HabitFrequency freq) {
        this.freq = freq;
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

    public void sortHabitLog() {
        int n = HabitLog.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (HabitLog.get(j).getYear() > HabitLog.get(j + 1).getYear()) {
                    HabitDateAndTime temp = HabitLog.get(j);
                    HabitLog.set(j, HabitLog.get(j + 1));
                    HabitLog.set(j + 1, temp);
                }
            }
        }

        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if ((HabitLog.get(j).getYear() == HabitLog.get(j+1).getYear()) && (HabitLog.get(j).getMonth() > HabitLog.get(j + 1).getMonth())) {
                    HabitDateAndTime temp = HabitLog.get(j);
                    HabitLog.set(j, HabitLog.get(j + 1));
                    HabitLog.set(j + 1, temp);
                }
            }
        }

        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if ((HabitLog.get(j).getYear() == HabitLog.get(j+1).getYear() && HabitLog.get(j).getMonth() == HabitLog.get(j + 1).getMonth())
                    && (HabitLog.get(j).getDay() > HabitLog.get(j + 1).getDay())) {
                    HabitDateAndTime temp = HabitLog.get(j);
                    HabitLog.set(j, HabitLog.get(j + 1));
                    HabitLog.set(j + 1, temp);
                }
            }
        }
    }

    public List<HabitDateAndTime> getHabitLog () {
        return HabitLog;
    }
}
