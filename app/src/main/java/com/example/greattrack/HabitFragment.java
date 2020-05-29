package com.example.greattrack;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class HabitFragment extends Fragment {
    private static final String TAG = "Habit Fragment";
    private List<Habit> habitList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        Button newHabitButton = view.findViewById(R.id.newHabitButton);
        newHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDialogue();
            }
        });
        return view;
    }

    private void goToDialogue() {
        Intent intent = new Intent(HabitFragment.this.getActivity(), HabitDialogueFragment.class);
        startActivity(intent);
    }

}
