package com.example.greattrack.habit;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.greattrack.MyCardView;
import com.example.greattrack.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitFragment extends Fragment {
    private static final String TAG = "Habit Fragment";
    private LinearLayout linearLayout = null;
    public static List<Habit> habitList = new ArrayList<Habit>();
    public static Map<Habit, int[]> remindersOnHabits = new HashMap<Habit, int[]>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "HabitFragment OnCreateView");
        Log.d("TAG", "habit list size before add new habit: " + habitList.size());
        if (getHabitList() != null) {
            habitList = getHabitList();
        }
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        Button newHabitButton = view.findViewById(R.id.newHabitButton);
        newHabitButton.setTextColor(Color.parseColor("#ffffff"));
        newHabitButton.setTextSize((float) 15.0);
        linearLayout = view.findViewById(R.id.linearLayoutInHabitScrollView);
        newHabitButton.setOnClickListener(v -> goToDialogue());

        Log.d("TAG", "return view");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        saveHabitList();
        createCards();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                final String result = data.getStringExtra("dateTimes");
                Log.d("TAG", "LOOK HERE: " + result);
            }
        }
    }

    public void createCards() {
        Log.d("TAG", "createCards method");
        Log.d("TAG", "linear layout has " + (linearLayout.getChildCount() - 1) + " card views");
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            Log.d("TAG", "create card deletion: iteration " + i);
            Log.d("TAG", "This one is of type " + linearLayout.getChildAt(i).getClass());
            if (linearLayout.getChildAt(i) instanceof MyCardView) {
                Log.d("TAG", "deleting cards, iteration number " + i);
                linearLayout.removeView(linearLayout.getChildAt(i));
                i--;
            }
        }
        if(habitList != null) {
            if (habitList.size() > 0) {
                Log.d("TAG", "habit list is of size " + habitList.size());
                for (Habit newHabit : habitList) {
                    Log.d("TAG", "adding to linearLayout, am in for loop");
                    MyCardView cardView = new MyCardView(this.getContext());
                    cardView.setId(View.generateViewId());
                    CardView.LayoutParams cardLayoutParams = new CardView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
                    cardLayoutParams.setMargins(20, 10, 20, 10);
                    cardView.setLayoutParams(cardLayoutParams);
                    //cardView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
                    cardView.setRadius((float) 20.0);
                    cardView.setBackgroundColor(Color.parseColor("#ffa500"));
                    TextView freqText = new TextView(this.getContext());
                    freqText.setTextSize((float) 15.0);

                    Log.d("TAG", "adding habit named " + newHabit.habitName);

                    String freqLabel = "";
                    String mightBePlural = " times every ";
                    if (newHabit.timesDuringFreq == 1) {
                        mightBePlural = " time every ";
                    }

                    switch (newHabit.freq) {
                        case hourly:
                            freqLabel = "hour";
                            break;
                        case halfHourly:
                            freqLabel = "30 minutes";
                            break;
                        case daily:
                            freqLabel = "day";
                            break;
                        case weekly:
                            freqLabel = "week";
                            break;
                        case biweekly:
                            freqLabel = "2 weeks";
                            break;
                        case monthly:
                            freqLabel = "month";
                            break;
                        case halfYearly:
                            freqLabel = "6 months";
                            break;
                        case yearly:
                            freqLabel = "year";
                            break;
                    }

                    freqText.setText(newHabit.timesDuringFreq + mightBePlural + freqLabel);
                    freqText.setId(View.generateViewId());

                    RelativeLayout rl = new RelativeLayout(this.getContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams infoButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    Button deleteButton = new Button(this.getContext());
                    deleteButton.setId(View.generateViewId());
                    Button infoButton = new Button(this.getContext());
                    infoButton.setId(View.generateViewId());
                    infoButton.setText("Log");
                    infoButton.setTextColor(Color.parseColor("#ffa500"));
                    infoButton.setTextSize((float) 14.0);
                    infoButton.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    deleteButton.setText("Delete");
                    deleteButton.setTextColor(Color.parseColor("#ffa500"));
                    deleteButton.setTextSize((float) 14.0);
                    deleteButton.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    TextView text = new TextView(this.getContext());
                    text.setTextColor(Color.parseColor("#ffffff"));
                    text.setText(newHabit.habitName);
                    text.setTextSize((float) 19.0);
                    text.setId(View.generateViewId());
                    layoutParams.setMargins(10, 0, 0, 0);
                    layoutParams2.setMargins(10, 0, 0, 0);
                    buttonLayoutParams.setMargins(8, 0,12,0);
                    infoButtonParams.setMargins(8,0,8,0);

                    rl.addView(text, layoutParams);
                    layoutParams2.addRule(RelativeLayout.BELOW, text.getId());
                    rl.addView(freqText, layoutParams2);
                    buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    buttonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    rl.addView(deleteButton, buttonLayoutParams);
                    infoButtonParams.addRule(RelativeLayout.LEFT_OF, deleteButton.getId());
                    infoButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    rl.addView(infoButton, infoButtonParams);
                    deleteButton.setOnClickListener(v -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                        builder.setTitle("Delete Habit \"" + newHabit.habitName + "\"");
                        builder.setMessage("Are you sure that you would like to delete this habit?");

                        builder.setPositiveButton("Yes, delete \"" + newHabit.habitName + "\"", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeHabit(newHabit, cardView);
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
                    });

                    infoButton.setOnClickListener(v -> goToLog(newHabit));

                    cardView.addView(rl);



                    linearLayout.addView(cardView);
                }
            } else {
                Log.d("TAG", "habit list size not changed");
            }
        }
    }

    private void removeHabit(Habit habit, CardView cardView) {
        habitList.remove(habit);
        linearLayout.removeView(cardView);
        saveHabitList();
        createCards();
    }

    private void goToDialogue() {
        Log.d("TAG", "HabitFragment goToDialogue");
        Intent intent = new Intent(HabitFragment.this.getActivity(), HabitDialogueActivity.class);
        startActivity(intent);
    }

    private void goToLog(Habit habit) {
        Log.d("TAG", "HabitFragment goToLog function");
        Intent intent = new Intent(HabitFragment.this.getActivity(), HabitLog.class);
        intent.putExtra("SentHabit", habit);
        startActivityForResult(intent, 0);
    }

    public void saveHabitList(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(habitList);
        editor.clear();
        editor.putString("habit list", json);
        editor.commit();
    }

    public ArrayList<Habit> getHabitList(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("habit list", null);
        Type type = new TypeToken<ArrayList<Habit>>() {}.getType();
        return gson.fromJson(json, type);
    }


//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
}
