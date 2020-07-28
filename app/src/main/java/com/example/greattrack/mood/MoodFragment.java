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
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

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
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            TextView textView = new TextView(this.getActivity());
            LinearLayout.LayoutParams textViewParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            String emotionText = "";
            Emoji emoji;
            switch (moodType) {
                case HAPPY:
                    emoji = EmojiManager.getForAlias("smile");
                    emotionText = emoji.getUnicode() + " Happy";
                    break;
                case CONTENT:
                    emoji = EmojiManager.getForAlias("relieved");
                    emotionText = emoji.getUnicode() + " Content";
                    break;
                case NEUTRAL:
                    emoji = EmojiManager.getForAlias("neutral_face");
                    emotionText = emoji.getUnicode() + " Neutral";
                    break;
                case SAD:
                    emoji = EmojiManager.getForAlias("pensive");
                    emotionText = emoji.getUnicode() + " Sad";
                    break;
                case AWFUL:
                    emoji = EmojiManager.getForAlias("weary");
                    emotionText = emoji.getUnicode() + " Awful";
                    break;
                case ANGRY:
                    emoji = EmojiManager.getForAlias("rage");
                    emotionText = emoji.getUnicode() + " Angry";
                    break;
                case TIRED:
                    emoji = EmojiManager.getForAlias("sleeping");
                    emotionText = emoji.getUnicode() +  " Tired";
                    break;
                case BORED:
                    emoji = EmojiManager.getForAlias("unamused");
                    emotionText = emoji.getUnicode() + " Bored";
                    break;
            }
            textView.setText(emotionText);
            cardView.setCardElevation(0);
            textView.setTextSize(25);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            cardView.setRadius(20);
            cardViewParams.setMargins(200, 30, 200, 0);
            cardView.addView(textView, textViewParams);
            linearLayout.addView(cardView, cardViewParams);
        }

        return view;
    }
}
