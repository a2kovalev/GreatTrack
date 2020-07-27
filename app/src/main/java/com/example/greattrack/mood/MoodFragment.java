package com.example.greattrack.mood;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.greattrack.R;

import org.w3c.dom.Text;

public class MoodFragment extends Fragment {

    private static final String TAG = "Mood Fragment";
    LinearLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood, container, false);
        Log.d("MTAG", "Mood fragment onCreate");
        linearLayout = view.findViewById(R.id.moodFragmentLinearLayout);
        for (moodTypes moodType : moodTypes.values()) {
            Log.d("MTAG", "adding mood: " + moodType.name());
            CardView cardView = new CardView(this.getActivity());
            LinearLayout.LayoutParams cardViewParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(this.getActivity());
            LinearLayout.LayoutParams textViewParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setText(moodType.toString());
            textView.setTextSize(25);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            cardViewParams.setMargins(200, 30, 200, 0);
            cardView.addView(textView, textViewParams);
            linearLayout.addView(cardView, cardViewParams);
        }

        return view;
    }
}
