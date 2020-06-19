package com.example.greattrack.habit;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class HabitDateAndTime implements Parcelable, Serializable {
    int hour, minute, year, month, day;

    public HabitDateAndTime(int hour, int minute, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    protected HabitDateAndTime(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
    }

    public static final Creator<HabitDateAndTime> CREATOR = new Creator<HabitDateAndTime>() {
        @Override
        public HabitDateAndTime createFromParcel(Parcel in) {
            return new HabitDateAndTime(in);
        }

        @Override
        public HabitDateAndTime[] newArray(int size) {
            return new HabitDateAndTime[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hour);
        dest.writeInt(this.minute);
        dest.writeInt(this.day);
        dest.writeInt(this.month);
        dest.writeInt(this.year);
    }
}
