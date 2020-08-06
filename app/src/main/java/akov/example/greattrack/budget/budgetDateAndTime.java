package akov.example.greattrack.budget;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class budgetDateAndTime implements Serializable {
    int hour, minute, year, month, day;
    double amount;

    public budgetDateAndTime(int hour, int minute, int year, int month, int day, double amount) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }
}
