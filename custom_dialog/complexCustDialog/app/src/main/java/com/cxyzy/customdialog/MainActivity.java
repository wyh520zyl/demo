package com.cxyzy.customdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(v -> {
            new VoiceDetectDialog(MainActivity.this, VoiceDetectDialog.Styles.LISTENING).show();
        });
        findViewById(R.id.button2).setOnClickListener(v -> {
            new VoiceDetectDialog(MainActivity.this, VoiceDetectDialog.Styles.DETECTING).show();
        });
        findViewById(R.id.button3).setOnClickListener(v -> {
            new VoiceDetectDialog(MainActivity.this, VoiceDetectDialog.Styles.RESULT_FAILED).show();
        });
    }
}
