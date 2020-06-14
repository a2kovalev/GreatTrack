package com.example.greattrack.habit;

public class HabitDateAndTime {
    int hour, minute, year, month, day;

    public HabitDateAndTime(int hour, int minute, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

}
