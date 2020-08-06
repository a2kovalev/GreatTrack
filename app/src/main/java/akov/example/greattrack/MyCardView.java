package akov.example.greattrack;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class MyCardView extends CardView {

    public MyCardView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        setCardBackgroundColor(backgroundColor);
    }
}
