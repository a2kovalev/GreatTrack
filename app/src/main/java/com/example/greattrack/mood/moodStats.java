package com.example.greattrack.mood;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.greattrack.R;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

public class moodStats extends AppCompatActivity {
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Mood Statistics");
        Log.d("TAG", "Mood stats activity");
        setContentView(R.layout.mood_stats_activity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10cc3f")));

        linearLayout = findViewById(R.id.moodStatsLinearLayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        CardView mostRecentMoodCard = (CardView) inflater.inflate(R.layout.mood_display_card, linearLayout, false);
        TextView recentMoodName = mostRecentMoodCard.findViewById(R.id.moodCardMoodName);
        TextView recentMoodDate = mostRecentMoodCard.findViewById(R.id.moodCardDate);

        if (!MoodFragment.moodStack.isEmpty()) {
            Emoji emoji;
            Mood recentMood = MoodFragment.moodStack.peek();
            String emotionText = "";
            switch (recentMood.getMood()) {
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
            recentMoodName.setText(emotionText);
            recentMoodDate.setText(recentMood.getDate().getMonth() + "/" + recentMood.getDate().getDay() + "/" + recentMood.getDate().getYear());
        }
        linearLayout.addView(mostRecentMoodCard);

    }
}
