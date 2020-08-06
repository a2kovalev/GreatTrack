package akov.example.greattrack.mood;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import akov.example.greattrack.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
        showMostRecentMood();
        showMostFrequentMood();
        showEachMoodFreq();
        monthlyMoodGraph();

        Button allMoodsButton = new Button(this);
        LinearLayout.LayoutParams allMoodsButtonParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        allMoodsButton.setText("Press to see full list of moods");
        allMoodsButton.setTextSize(12);
        allMoodsButton.setTransformationMethod(null);
        allMoodsButton.setBackgroundResource(R.drawable.round_btn);
        allMoodsButton.setTextColor(Color.parseColor("#FFFFFF"));
        allMoodsButtonParams.setMargins(0, 30, 0, 0);
        allMoodsButton.setPadding(50, 15, 50, 15);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        allMoodsButton.setOnClickListener(v -> {
            goToAllMoods();
        });
        linearLayout.addView(allMoodsButton, allMoodsButtonParams);
    }

    private void showMostRecentMood() {
        LayoutInflater inflater = LayoutInflater.from(this);
        CardView mostRecentMoodCard = (CardView) inflater.inflate(R.layout.mood_display_card, linearLayout, false);
        TextView recentMoodName = mostRecentMoodCard.findViewById(R.id.moodCardMoodName);
        TextView recentMoodDate = mostRecentMoodCard.findViewById(R.id.moodCardDate);
        TextView recentMoodExplanation = mostRecentMoodCard.findViewById(R.id.moodCardExplain);

        if (!MoodFragment.moodStack.isEmpty()) {
            Emoji emoji;
            Mood recentMood = MoodFragment.moodStack.peek();
            String emotionText = "";
            RelativeLayout relativeLayout = mostRecentMoodCard.findViewById(R.id.moodCardRelativeLayout);
            switch (recentMood.getMood()) {
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
                    relativeLayout.setBackgroundColor(Color.parseColor("#a88218"));
                    break;
                case ANGRY:
                    emoji = EmojiManager.getForAlias("rage");
                    emotionText = emoji.getUnicode() + " Angry";
                    relativeLayout.setBackgroundColor(Color.parseColor("#c93204"));
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
            recentMoodName.setText(emotionText);
            recentMoodDate.setText(recentMood.getDate().getMonth() + "/" + recentMood.getDate().getDay() + "/" + recentMood.getDate().getYear());
            recentMoodExplanation.setText("Description: " + recentMood.getExplanation());
        }
        linearLayout.addView(mostRecentMoodCard, 0);
    }

    private void showMostFrequentMood() {
        LayoutInflater inflater = LayoutInflater.from(this);
        CardView mostFrequentMoodCard = (CardView) inflater.inflate(R.layout.mood_display_card, linearLayout, false);
        TextView frequentMoodCardTitle = mostFrequentMoodCard.findViewById(R.id.moodCardTitle);
        TextView frequentMoodName = mostFrequentMoodCard.findViewById(R.id.moodCardMoodName);

        //Hiding these two
        TextView recentMoodDate = mostFrequentMoodCard.findViewById(R.id.moodCardDate);
        TextView recentMoodExplanation = mostFrequentMoodCard.findViewById(R.id.moodCardExplain);
        ((ViewGroup)recentMoodDate.getParent()).removeView(recentMoodDate);
        ((ViewGroup)recentMoodExplanation.getParent()).removeView(recentMoodExplanation);

        frequentMoodCardTitle.setText("Overall Most Frequent Mood");

        HashMap<moodTypes, Integer> moodFreqs = new HashMap();
        int numHappy=0, numContent=0, numNeutral=0, numSad=0, numAwful=0, numAngry=0, numTired=0, numBored=0;

        if (!MoodFragment.moodStack.isEmpty()) {
            for (Mood curr : MoodFragment.moodStack) {
                moodTypes moodType = curr.getMood();
                switch (moodType) {
                    case HAPPY:
                        ++numHappy;
                        break;
                    case CONTENT:
                        ++numContent;
                        break;
                    case NEUTRAL:
                        ++numNeutral;
                        break;
                    case SAD:
                        ++numSad;
                        break;
                    case ANGRY:
                        ++numAngry;
                        break;
                    case AWFUL:
                        ++numAwful;
                        break;
                    case TIRED:
                        ++numTired;
                        break;
                    case BORED:
                        ++numBored;
                        break;
                }
            }
            moodFreqs.put(moodTypes.ANGRY, numAngry);
            moodFreqs.put(moodTypes.AWFUL, numAwful);
            moodFreqs.put(moodTypes.BORED, numBored);
            moodFreqs.put(moodTypes.CONTENT, numContent);
            moodFreqs.put(moodTypes.HAPPY, numHappy);
            moodFreqs.put(moodTypes.NEUTRAL, numNeutral);
            moodFreqs.put(moodTypes.SAD, numSad);
            moodFreqs.put(moodTypes.TIRED, numTired);

            moodTypes maxMood = null;
            for(Map.Entry<moodTypes, Integer> entry : moodFreqs.entrySet()) {
                if (entry.getValue() == Collections.max(moodFreqs.values())) {
                    maxMood = entry.getKey();
                }
            }
            if (maxMood != null) {
                Log.d("MTAG", "max: " + maxMood);

                String emotionText = "";
                Emoji emoji;
                RelativeLayout relativeLayout = mostFrequentMoodCard.findViewById(R.id.moodCardRelativeLayout);
                switch (maxMood) {
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
                        relativeLayout.setBackgroundColor(Color.parseColor("#a88218"));
                        break;
                    case ANGRY:
                        emoji = EmojiManager.getForAlias("rage");
                        emotionText = emoji.getUnicode() + " Angry";
                        relativeLayout.setBackgroundColor(Color.parseColor("#c93204"));
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
                frequentMoodName.setText(emotionText);
                linearLayout.addView(mostFrequentMoodCard, 1);
            }
        }
    }


    private int getMaxMoodValue() {
        HashMap<moodTypes, Integer> moodFreqs = new HashMap();
        int numHappy=0, numContent=0, numNeutral=0, numSad=0, numAwful=0, numAngry=0, numTired=0, numBored=0;

        for (Mood curr : MoodFragment.moodStack) {
                moodTypes moodType = curr.getMood();
                switch (moodType) {
                    case HAPPY:
                        ++numHappy;
                        break;
                    case CONTENT:
                        ++numContent;
                        break;
                    case NEUTRAL:
                        ++numNeutral;
                        break;
                    case SAD:
                        ++numSad;
                        break;
                    case ANGRY:
                        ++numAngry;
                        break;
                    case AWFUL:
                        ++numAwful;
                        break;
                    case TIRED:
                        ++numTired;
                        break;
                    case BORED:
                        ++numBored;
                        break;
                }
            }
            moodFreqs.put(moodTypes.ANGRY, numAngry);
            moodFreqs.put(moodTypes.AWFUL, numAwful);
            moodFreqs.put(moodTypes.BORED, numBored);
            moodFreqs.put(moodTypes.CONTENT, numContent);
            moodFreqs.put(moodTypes.HAPPY, numHappy);
            moodFreqs.put(moodTypes.NEUTRAL, numNeutral);
            moodFreqs.put(moodTypes.SAD, numSad);
            moodFreqs.put(moodTypes.TIRED, numTired);

            return Collections.max(moodFreqs.values());
    }

    private void showEachMoodFreq() {
        Log.d("MTAG", "In bar chart function");
        BarChart barChart = findViewById(R.id.moodFreqGraph);

        HashMap<moodTypes, Integer> moodFreqs = new HashMap();
        int numHappy=0, numContent=0, numNeutral=0, numSad=0, numAwful=0, numAngry=0, numTired=0, numBored=0;

        if (!MoodFragment.moodStack.isEmpty()) {
            for (Mood curr : MoodFragment.moodStack) {
                moodTypes moodType = curr.getMood();
                switch (moodType) {
                    case HAPPY:
                        ++numHappy;
                        break;
                    case CONTENT:
                        ++numContent;
                        break;
                    case NEUTRAL:
                        ++numNeutral;
                        break;
                    case SAD:
                        ++numSad;
                        break;
                    case ANGRY:
                        ++numAngry;
                        break;
                    case AWFUL:
                        ++numAwful;
                        break;
                    case TIRED:
                        ++numTired;
                        break;
                    case BORED:
                        ++numBored;
                        break;
                }
            }
            moodFreqs.put(moodTypes.ANGRY, numAngry);
            moodFreqs.put(moodTypes.AWFUL, numAwful);
            moodFreqs.put(moodTypes.BORED, numBored);
            moodFreqs.put(moodTypes.CONTENT, numContent);
            moodFreqs.put(moodTypes.HAPPY, numHappy);
            moodFreqs.put(moodTypes.NEUTRAL, numNeutral);
            moodFreqs.put(moodTypes.SAD, numSad);
            moodFreqs.put(moodTypes.TIRED, numTired);

            barChart.getDescription().setEnabled(false);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getAxisRight().setEnabled(false);
            barChart.getAxisLeft().setLabelCount(getMaxMoodValue());
            List<BarEntry> entries = new ArrayList<>();

            entries.add(new BarEntry(0, moodFreqs.get(moodTypes.HAPPY)));
            entries.add(new BarEntry(1, moodFreqs.get(moodTypes.CONTENT)));
            entries.add(new BarEntry(2, moodFreqs.get(moodTypes.NEUTRAL)));
            entries.add(new BarEntry(3, moodFreqs.get(moodTypes.SAD)));
            entries.add(new BarEntry(4, moodFreqs.get(moodTypes.AWFUL)));
            entries.add(new BarEntry(5, moodFreqs.get(moodTypes.ANGRY)));
            entries.add(new BarEntry(6, moodFreqs.get(moodTypes.TIRED)));
            entries.add(new BarEntry(7, moodFreqs.get(moodTypes.BORED)));

            final String[] moodNames = new String[] { EmojiManager.getForAlias("smile").getUnicode(),
                    EmojiManager.getForAlias("relieved").getUnicode(),
                    EmojiManager.getForAlias("neutral_face").getUnicode(),
                    EmojiManager.getForAlias("pensive").getUnicode(),
                    EmojiManager.getForAlias("weary").getUnicode(),
                    EmojiManager.getForAlias("rage").getUnicode(),
                    EmojiManager.getForAlias("sleeping").getUnicode(),
                    EmojiManager.getForAlias("unamused").getUnicode()};
            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return moodNames[(int) value];
                }
            };
            barChart.setExtraTopOffset(5);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setTextSize(14);
            xAxis.setGranularity(1); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

            BarDataSet set = new BarDataSet(entries, "Mood Frequency");
            set.setDrawValues(false);
            set.setColors(new int[] {
                    Color.parseColor("#0080ff"),
                    Color.parseColor("#18a8a3"),
                    Color.parseColor("#45bd00"),
                    Color.parseColor("#97a818"),
                    Color.parseColor("#a88218"),
                    Color.parseColor("#c93204"),
                    Color.parseColor("#7a04cf"),
                    Color.parseColor("#a3a3a3")});

            BarData data = new BarData(set);
            data.setBarWidth(0.9f); // set custom bar width
            barChart.setData(data);
            barChart.setFitBars(true); // make the x-axis fit exactly all bars
            barChart.invalidate(); // refresh

        }
    }

    private void monthlyMoodGraph() {
        Stack<Mood> moods = moodsThisMonth();
        HorizontalBarChart barChart = findViewById(R.id.moodMonthFreqGraph);
        HashMap<moodTypes, Integer> moodFreqs = new HashMap();
        int numHappy=0, numContent=0, numNeutral=0, numSad=0, numAwful=0, numAngry=0, numTired=0, numBored=0;

        if (!moods.isEmpty()) {
            for (Mood curr : moods) {
                moodTypes moodType = curr.getMood();
                switch (moodType) {
                    case HAPPY:
                        ++numHappy;
                        break;
                    case CONTENT:
                        ++numContent;
                        break;
                    case NEUTRAL:
                        ++numNeutral;
                        break;
                    case SAD:
                        ++numSad;
                        break;
                    case ANGRY:
                        ++numAngry;
                        break;
                    case AWFUL:
                        ++numAwful;
                        break;
                    case TIRED:
                        ++numTired;
                        break;
                    case BORED:
                        ++numBored;
                        break;
                }
            }
            moodFreqs.put(moodTypes.ANGRY, numAngry);
            moodFreqs.put(moodTypes.AWFUL, numAwful);
            moodFreqs.put(moodTypes.BORED, numBored);
            moodFreqs.put(moodTypes.CONTENT, numContent);
            moodFreqs.put(moodTypes.HAPPY, numHappy);
            moodFreqs.put(moodTypes.NEUTRAL, numNeutral);
            moodFreqs.put(moodTypes.SAD, numSad);
            moodFreqs.put(moodTypes.TIRED, numTired);

            barChart.getDescription().setEnabled(false);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getAxisRight().setEnabled(false);
            List<BarEntry> entries = new ArrayList<>();

            entries.add(new BarEntry(0, moodFreqs.get(moodTypes.HAPPY)));
            entries.add(new BarEntry(1, moodFreqs.get(moodTypes.CONTENT)));
            entries.add(new BarEntry(2, moodFreqs.get(moodTypes.NEUTRAL)));
            entries.add(new BarEntry(3, moodFreqs.get(moodTypes.SAD)));
            entries.add(new BarEntry(4, moodFreqs.get(moodTypes.AWFUL)));
            entries.add(new BarEntry(5, moodFreqs.get(moodTypes.ANGRY)));
            entries.add(new BarEntry(6, moodFreqs.get(moodTypes.TIRED)));
            entries.add(new BarEntry(7, moodFreqs.get(moodTypes.BORED)));

            final String[] moodNames = new String[] { EmojiManager.getForAlias("smile").getUnicode(),
                    EmojiManager.getForAlias("relieved").getUnicode(),
                    EmojiManager.getForAlias("neutral_face").getUnicode(),
                    EmojiManager.getForAlias("pensive").getUnicode(),
                    EmojiManager.getForAlias("weary").getUnicode(),
                    EmojiManager.getForAlias("rage").getUnicode(),
                    EmojiManager.getForAlias("sleeping").getUnicode(),
                    EmojiManager.getForAlias("unamused").getUnicode()};
            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return moodNames[(int) value];
                }
            };
            barChart.setExtraTopOffset(5);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setTextSize(10);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

            BarDataSet set = new BarDataSet(entries, "Mood Frequency");
            set.setColors(new int[] {
                    Color.parseColor("#0080ff"),
                    Color.parseColor("#18a8a3"),
                    Color.parseColor("#45bd00"),
                    Color.parseColor("#97a818"),
                    Color.parseColor("#a88218"),
                    Color.parseColor("#c93204"),
                    Color.parseColor("#7a04cf"),
                    Color.parseColor("#a3a3a3")});

            BarData data = new BarData(set);
            data.setBarWidth(0.9f); // set custom bar width
            barChart.setData(data);
            barChart.setFitBars(true); // make the x-axis fit exactly all bars
            barChart.invalidate(); // refresh

        }
    }

    private Stack<Mood> moodsThisMonth() {
        Log.d("MTAG", "moods this month method");
        Stack<Mood> moods = new Stack<>();
        LocalDate date = LocalDate.now();
        for (Mood mood : MoodFragment.moodStack) {
            if (mood.getDate().getMonth() == date.getMonthValue() && mood.getDate().getYear() == date.getYear()) {
                moods.push(mood);
            }
        }
        Log.d("MTAG", "number of moods recorded this month: " + moods.size() + 1);
        return moods;
    }

    private void goToAllMoods() {
        Log.d("MTAG", "going to all moods");
        Intent intent = new Intent(moodStats.this, allMoodsDisplay.class);
        startActivity(intent);
    }
}
