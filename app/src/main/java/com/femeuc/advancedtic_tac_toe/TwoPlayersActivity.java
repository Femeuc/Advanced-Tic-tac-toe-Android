package com.femeuc.advancedtic_tac_toe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TwoPlayersActivity extends AppCompatActivity {
    private int turnValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_players);
        turnValue = 1;

    }

    public void markSquare(View v) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if(turnValue % 2 != 0) {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.cross_sign));
            } else {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_sign));
            }
        } else {
            if(turnValue % 2 != 0) {
                v.setBackground(getResources().getDrawable(R.drawable.cross_sign));
            } else {
                v.setBackground(getResources().getDrawable(R.drawable.circle_sign));
            }
        }
        turnValue++;
    }

    public int getTurnValue() {
        return turnValue;
    }

    public void setTurnValue(int v) {
        turnValue = v;
    }

}
