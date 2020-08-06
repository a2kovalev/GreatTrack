package com.example.greattrack.mood;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.greattrack.R;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.util.Stack;

public class allMoodsDisplay extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_moods_display);
        Log.d("MTAG", "in all moods display onCreate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#45bd00")));
        getSupportActionBar().setTitle("All Recorded Moods");
        LinearLayout linearLayout = findViewById(R.id.allMoodsLinearLayout);

        Stack<Mood> reverseMoods = new Stack<>();

        for (int i = MoodFragment.moodStack.size() - 1; i >= 0; i--) {
            reverseMoods.add(MoodFragment.moodStack.get(i));
        }

        for (Mood mood : reverseMoods) {
            LayoutInflater inflater = LayoutInflater.from(this);
            CardView cardView = (CardView) inflater.inflate(R.layout.mood_display_card, linearLayout, false);
            TextView cardTitle = cardView.findViewById(R.id.moodCardTitle);
            TextView cardDate = cardView.findViewById(R.id.moodCardDate);
            TextView cardMoodName = cardView.findViewById(R.id.moodCardMoodName);
            TextView cardExplanation = cardView.findViewById(R.id.moodCardExplain);

            cardTitle.setText(mood.getDate().getMonth() + "/" + mood.getDate().getDay() + "/" + mood.getDate().getYear());
            cardDate.setTextSize(1);
            cardDate.setVisibility(View.INVISIBLE);

            RelativeLayout relativeLayout = cardView.findViewById(R.id.moodCardRelativeLayout);
            Emoji emoji;
            String emotionText = "";
            switch (mood.getMood()) {
                case HAPPY:
                    emoji = EmojiManager.getForAlias("smile");
                    emotionText = emoji.getUnicode() + " Happy";
                    relativeLayout.setBackgroundColor(Color.parseColor("#0080ff"));
                    break;
                case CONTENT:
                    emoji = EmojiManager.getForAlias("relieved");
                    emotionText = emoji.getUnicode() + " Content";
                    relativeLayout.setBackgroundColor(Color.parseColor("#18a8a3"));
                    break;
                case NEUTRAL:
                    emoji = EmojiManager.getForAlias("neutral_face");
                    emotionText = emoji.getUnicode() + " Neutral";
                    relativeLayout.setBackgroundColor(Color.parseColor("#45bd00"));
                    break;
                case SAD:
                    emoji = EmojiManager.getForAlias("pensive");
                    emotionText = emoji.getUnicode() + " Sad";
                    relativeLayout.setBackgroundColor(Color.parseColor("#97a818"));
                    break;
                case AWFUL:
                    emoji = EmojiManager.getForAlias("weary");
                    emotionText = emoji.getUnicode() + " Awful";
                    relativeLayout.setBackgroundColor(Color.parseColor("#c93204"));
                    break;
                case ANGRY:
                    emoji = EmojiManager.getForAlias("rage");
                    emotionText = emoji.getUnicode() + " Angry";
                    break;
                case TIRED:
                    emoji = EmojiManager.getForAlias("sleeping");
                    emotionText = emoji.getUnicode() +  " Tired";
                    relativeLayout.setBackgroundColor(Color.parseColor("#7a04cf"));
                    break;
                case BORED:
                    emoji = EmojiManager.getForAlias("unamused");
                    emotionText = emoji.getUnicode() + " Bored";
                    relativeLayout.setBackgroundColor(Color.parseColor("#a3a3a3"));
                    break;
            }
            cardMoodName.setText(emotionText);
            cardExplanation.setText(mood.getExplanation());
            linearLayout.addView(cardView);
        }
    }
}
