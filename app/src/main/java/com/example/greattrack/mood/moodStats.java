package com.example.greattrack.mood;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greattrack.R;

public class moodStats extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Mood Statistics");
        Log.d("TAG", "Mood stats activity");
        setContentView(R.layout.mood_stats_activity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10cc3f")));
    }
}
