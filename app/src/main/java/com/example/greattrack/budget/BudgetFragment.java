package com.example.greattrack.budget;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.greattrack.R;

public class BudgetFragment extends Fragment {
    private static final String TAG = "Budget Fragment";
    public static boolean thereIsABudget = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_budget, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.budgetRelativeLayout);
        Button createBudgetButton = new Button(this.getActivity());
        CardView buttonCardView = new CardView(this.getActivity());
        RelativeLayout.LayoutParams buttonCardParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams createButtonParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        createBudgetButton.setText("Create your budget");
        createBudgetButton.setTextSize((float) 20.0);
        createBudgetButton.setStateListAnimator(null);
        createBudgetButton.setTextColor(Color.parseColor("#FFFFFF"));
        createBudgetButton.setBackgroundColor(Color.parseColor("#10cc3f"));
        buttonCardView.setCardBackgroundColor(Color.parseColor("#10cc3f"));
        createButtonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        createButtonParams.setMargins(15, 0, 15, 0);
        buttonCardView.setRadius((float) 50.0);
        buttonCardParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        buttonCardView.addView(createBudgetButton, createButtonParams);

        if (thereIsABudget == false) {
            relativeLayout.addView(buttonCardView, 0, buttonCardParams);

            createBudgetButton.setOnClickListener(v -> {
                Log.d("TAG", "Create budget button clicked");
                createBudgetButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonCardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                goToCreateBudget();
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(thereIsABudget==true) {
            RelativeLayout relativeLayout = getView().findViewById(R.id.budgetRelativeLayout);
            relativeLayout.removeViewAt(0);
        }
    }

    public void goToCreateBudget() {
        Log.d("TAG", "Go to create budget");
        Intent intent = new Intent(BudgetFragment.this.getActivity(), createBudget.class);
        startActivity(intent);
    }
}
