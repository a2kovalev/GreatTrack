package akov.example.greattrack.mood;

import java.io.Serializable;

public class MoodDate implements Serializable {
    int day;
    int month;
    int year;

    public MoodDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
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
