package com.femeuc.advancedtic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.femeuc.advancedtic_tac_toe.activities.BluetoothMultiplayer;
import com.femeuc.advancedtic_tac_toe.activities.TwoPlayersActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showTwoPlayersActivity(View view) {
        Intent intent = new Intent(this, TwoPlayersActivity.class);
        startActivity(intent);
    }

    public void openBluetoothMultiplayerActivity(View view) {
        Intent intent = new Intent(this, BluetoothMultiplayer.class);
        startActivity(intent);
    }
}
