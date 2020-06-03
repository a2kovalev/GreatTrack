package com.example.greattrack;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class HabitFragment extends Fragment {
    private static final String TAG = "Habit Fragment";
    public int habitListSize = HabitDialogueFragment.getHabitListSize();
    private LinearLayout linearLayout = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "HabitFragment OnCreateView");
        Log.d("TAG", "habit list size before add new habit: " + habitListSize);
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        Button newHabitButton = view.findViewById(R.id.newHabitButton);
        linearLayout = view.findViewById(R.id.linearLayoutInHabitScrollView);
        newHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDialogue();
            }
        });

        Log.d("TAG", "return view");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (HabitDialogueFragment.getHabitListSize() > habitListSize) {
            Log.d("TAG", "adding to linearLayout, am in if statement");
            habitListSize = HabitDialogueFragment.getHabitListSize();
            Habit newHabit = HabitDialogueFragment.habitList.get(habitListSize - 1);
            TextView text = new TextView(this.getContext());
            text.setText(newHabit.habitName);
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(text);
        } else {
            Log.d("TAG", "habit list size not changed");
        }
    }

    private void goToDialogue() {
        Log.d("TAG", "HabitFragment goToDialogue");
        Intent intent = new Intent(HabitFragment.this.getActivity(), HabitDialogueFragment.class);
        startActivity(intent);
    }

}
