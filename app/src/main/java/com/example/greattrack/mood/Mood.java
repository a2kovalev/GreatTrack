package com.example.greattrack.mood;

import java.io.Serializable;
import java.util.Objects;

enum moodTypes implements  Serializable {
    HAPPY, CONTENT, NEUTRAL, SAD, AWFUL, ANGRY, TIRED, BORED
}

public class Mood implements Serializable {
    moodTypes mood;
    MoodDate date;
    String explanation;

    public Mood (moodTypes mood, MoodDate date, String explanation) {
        this.mood = mood;
        this.date = date;
        this.explanation = explanation;
    }

    public moodTypes getMood() {
        return mood;
    }

    public MoodDate getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

}
