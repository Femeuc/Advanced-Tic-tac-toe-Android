package com.femeuc.advancedtic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String TYPE_OF_MATCH = "com.femeuc.advancedtic_tac_toe.TYPE_OF_MATCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showTwoPlayersActivity(View view) {
        Intent intent = new Intent(this, TwoPlayersActivity.class);
        int type_of_match = 1;
        intent.putExtra(TYPE_OF_MATCH, type_of_match);
        startActivity(intent);
    }

    public void openBluetoothMultiplayerActivity(View view) {
        Intent intent = new Intent(this, BluetoothMultiplayer.class);
        startActivity(intent);
    }
}
