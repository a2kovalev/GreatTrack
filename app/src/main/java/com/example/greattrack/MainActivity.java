package com.example.greattrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.greattrack.budget.BudgetFragment;
import com.example.greattrack.habit.HabitFragment;
import com.example.greattrack.mood.MoodFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10cc3f")));
        getSupportActionBar().setTitle("GreatTrack Lifestyle Tracker");

        SectionsPageAdapter mSectionsPageAdapter;
        final CustomViewPager mViewPager = (CustomViewPager) findViewById(R.id.vpPager);
        setupViewPager(mViewPager);
        mViewPager.disableScroll(true);
        mViewPager.setCurrentItem(0);
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.habitItem:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.budgetItem:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.moodItem:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    private void setupViewPager(CustomViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HabitFragment());
        adapter.addFragment(new BudgetFragment());
        adapter.addFragment(new MoodFragment());
        viewPager.setAdapter(adapter);
    }

}
