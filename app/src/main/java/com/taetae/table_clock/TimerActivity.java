package com.taetae.table_clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TimerActivity extends AppCompatActivity {
    Button btn_start,btn_stop,btn_reset,btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        btn_start = findViewById(R.id.startBtn);
        btn_stop = findViewById(R.id.stopBtn);
        btn_reset = findViewById(R.id.resetBtn);
        btn_back = findViewById(R.id.backActivity);

        btn_back.setOnClickListener(new View.OnClickListener() {  //액티비티 전환
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}