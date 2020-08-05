package com.example.greattrack.mood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.greattrack.R;
import com.example.greattrack.habit.Habit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Stack;

public class MoodFragment extends Fragment {

    private static final String TAG = "Mood Fragment";
    LinearLayout linearLayout;
    protected static Stack<Mood> moodStack = new Stack<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood, container, false);
        Log.d("MTAG", "Mood fragment onCreate");
        linearLayout = view.findViewById(R.id.moodFragmentLinearLayout);
        if (getMoodStack() != null) {
            moodStack = getMoodStack();
        }
        Log.d("MTAG", "Mood stack at start: " + moodStack);
        for (moodTypes moodType : moodTypes.values()) {
            Log.d("MTAG", "adding mood: " + moodType.name());
            CardView cardView = new CardView(this.getActivity());
            LinearLayout.LayoutParams cardViewParams =
                    new LinearLayout.LayoutParams(700, 200);
            TextView textView = new TextView(this.getActivity());
            textView.setTextColor(Color.parseColor("#f3f3f3"));
            LinearLayout.LayoutParams textViewParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            String emotionText = "";
            Emoji emoji;
            switch (moodType) {
                case HAPPY:
                    emoji = EmojiManager.getForAlias("smile");
                    emotionText = emoji.getUnicode() + " Happy";
                    cardView.setCardBackgroundColor(Color.parseColor("#0080ff"));
                    break;
                case CONTENT:
                    emoji = EmojiManager.getForAlias("relieved");
                    emotionText = emoji.getUnicode() + " Content";
                    cardView.setCardBackgroundColor(Color.parseColor("#18a8a3"));
                    break;
                case NEUTRAL:
                    emoji = EmojiManager.getForAlias("neutral_face");
                    emotionText = emoji.getUnicode() + " Neutral";
                    cardView.setCardBackgroundColor(Color.parseColor("#45bd00"));
                    break;
                case SAD:
                    emoji = EmojiManager.getForAlias("pensive");
                    emotionText = emoji.getUnicode() + " Sad";
                    cardView.setCardBackgroundColor(Color.parseColor("#97a818"));
                    break;
                case AWFUL:
                    emoji = EmojiManager.getForAlias("weary");
                    emotionText = emoji.getUnicode() + " Awful";
                    cardView.setCardBackgroundColor(Color.parseColor("#a88218"));
                    break;
                case ANGRY:
                    emoji = EmojiManager.getForAlias("rage");
                    emotionText = emoji.getUnicode() + " Angry";
                    cardView.setCardBackgroundColor(Color.parseColor("#c93204"));
                    break;
                case TIRED:
                    emoji = EmojiManager.getForAlias("sleeping");
                    emotionText = emoji.getUnicode() +  " Tired";
                    cardView.setCardBackgroundColor(Color.parseColor("#7a04cf"));
                    break;
                case BORED:
                    emoji = EmojiManager.getForAlias("unamused");
                    emotionText = emoji.getUnicode() + " Bored";
                    cardView.setCardBackgroundColor(Color.parseColor("#a3a3a3"));
                    break;
            }
            textView.setText(emotionText);
            cardView.setCardElevation(0);
            textView.setTextSize(25);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            cardView.setRadius(20);
            cardViewParams.gravity = Gravity.CENTER_HORIZONTAL;
            cardViewParams.setMargins(20, 15, 20, 15);
            cardView.addView(textView, textViewParams);

            cardView.setOnClickListener(v -> {
                boolean moodRecordedToday = false;
                for (Mood tempMood : moodStack) {
                    MoodDate moodDate = tempMood.getDate();
                    LocalDate rightNow = LocalDate.now();
                    if (moodDate.day == rightNow.getDayOfMonth() && moodDate.month == rightNow.getMonthValue() && moodDate.year == rightNow.getYear()) {
                        moodRecordedToday = true;
                    }
                }

                if (!moodRecordedToday) {
                    recordMoodAlert(moodType);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder((this.getContext()));
                    builder.setTitle("Override saved mood?");
                    builder.setMessage("You have already recorded a mood for today. Are you sure you want to replace it?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recordMoodAlert(moodType);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog2 = builder.create();
                    dialog2.show();
                }
            });

            linearLayout.addView(cardView, cardViewParams);
        }

        CardView cardView = new CardView(this.getActivity());
        LinearLayout.LayoutParams cardViewParams =
                new LinearLayout.LayoutParams(700, 200);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        TextView textView = new TextView(this.getActivity());
        textView.setTextColor(Color.parseColor("#f3f3f3"));
        LinearLayout.LayoutParams textViewParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setText("View Stats");
        cardView.setCardElevation(0);
        textView.setTextSize(25);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        cardView.setRadius(20);
        cardViewParams.gravity = Gravity.CENTER_HORIZONTAL;
        cardViewParams.setMargins(20, 15, 20, 15);
        cardView.addView(textView, textViewParams);
        cardView.setOnClickListener(v -> {
            if (!moodStack.isEmpty()) {
                goToStats();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("No stats yet!");
                builder.setMessage("To view stats, add your first mood!");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        linearLayout.addView(cardView, cardViewParams);

        return view;
    }

    private void recordMoodAlert(moodTypes moodType) {
        View alertView = getLayoutInflater().inflate(R.layout.add_mood_alert, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setView(alertView);
        builder.setTitle("You selected \"" + moodType.name() + "\"");
        builder.setMessage("If you want, add a short description below! Max. 160 chars");
        EditText moodDescription = (EditText) alertView.findViewById(R.id.addMoodEditText);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moodStack.pop();
                LocalDate currDate = LocalDate.now();
                MoodDate moodDate = new MoodDate(currDate.getDayOfMonth(), currDate.getMonthValue(), currDate.getYear());
                String moodExplanation = moodDescription.getText().toString();
                Mood newMood = new Mood(moodType, moodDate, moodExplanation);
                Log.d("MTAG", "Adding mood with explanation: " + newMood.explanation + ", Date: "
                        + newMood.getDate().getMonth() + "/" + newMood.getDate().getDay() + "/" + newMood.getDate().getYear());
                moodStack.push(newMood);
                Log.d("MTAG", "Sack after moodList push method: " + moodStack);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToStats() {
        Log.d("MTAG", "goToStats method");
        Intent intent = new Intent(MoodFragment.this.getActivity(), moodStats.class);
        startActivity(intent);
    }

    public static final String SHARED_PREFS = "MOOD_SP";
    public static final String NAME_OF_VAL = "mood_stack";

    private void saveMoodStack() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS, getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(moodStack);
        editor.putString(NAME_OF_VAL, json);
        Log.d("MTAG", "json in saveMoodStack: " + json);
        editor.apply();
    }

    public Stack<Mood> getMoodStack(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS, getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(NAME_OF_VAL, null);
        Type type = new TypeToken<Stack<Mood>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveMoodStack();
    }
}
