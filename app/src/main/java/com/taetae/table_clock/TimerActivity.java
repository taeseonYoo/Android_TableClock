package com.taetae.table_clock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {
    Button btn_start,btn_stop,btn_reset,btn_back;
    Chronometer cm;
    RelativeLayout rl;
    boolean flag=false;
    boolean running = false;
    long saveTime =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        btn_start = findViewById(R.id.startBtn);
        btn_stop = findViewById(R.id.stopBtn);
        btn_reset = findViewById(R.id.resetBtn);
        btn_back = findViewById(R.id.backActivity);
        cm = findViewById(R.id.chronometer);
        rl = findViewById(R.id.layout_t);

        rl.setOnClickListener(new View.OnClickListener() { //chronometer changes form after an hour.
            @Override                                      //ex> x:xx:xx
            public void onClick(View v) {
                if(!flag){
                    btn_start.setVisibility(View.INVISIBLE);
                    btn_stop.setVisibility(View.INVISIBLE);
                    btn_reset.setVisibility(View.INVISIBLE);
                    btn_back.setVisibility(View.INVISIBLE);
                    flag = true;
                }
                else{
                    btn_start.setVisibility(View.VISIBLE);
                    btn_stop.setVisibility(View.VISIBLE);
                    btn_reset.setVisibility(View.VISIBLE);
                    btn_back.setVisibility(View.VISIBLE);
                    flag = false;
                }
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!running) {
                    cm.setBase(SystemClock.elapsedRealtime() - saveTime);
                    cm.start();
                    btn_start.setEnabled(false);
                    btn_stop.setEnabled(true);
                    btn_reset.setEnabled(true);
                    running=true;
                }
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running) {
                    cm.stop();
                    saveTime = SystemClock.elapsedRealtime() - cm.getBase();
                    btn_start.setEnabled(true);
                    running = false;
                }
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setEnabled(true);
                btn_stop.setEnabled(false);
                btn_reset.setEnabled(false);
                cm.setBase(SystemClock.elapsedRealtime());
                saveTime=0;
                running=false;
                cm.stop();
            }
        });

        //chronometer works like this  (nr_of_min * 60000 + nr_of_sec * 1000)

        btn_back.setOnClickListener(new View.OnClickListener() {  //액티비티 전환
            long tmp,tmp2,tmp3;
            @Override
            public void onClick(View v) {
                if(saveTime!=0){ //When data remains ,saving the data in the dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this);
                    builder.setTitle("Save Data");
                    builder.setIcon(R.drawable.ic_save);
                    tmp = saveTime/60000;
                    tmp2 = saveTime%60000/1000;
                    if(tmp>60){
                        tmp3=tmp/60;
                        tmp%=60;

                        builder.setMessage("저장 할 데이터 : "+String.format("%02d",tmp3)+"시간"+String.format("%02d",tmp)+"분"+String.format("%02d",tmp2)+"초");

                    }
                    else{
                        builder.setMessage("저장 할 데이터 : "+String.format("%02d",tmp)+"분"+String.format("%02d",tmp2)+"초");
                    }
                    builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                            returnActivity();
                        }
                    });
                    builder.setNegativeButton("저장하지 않기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            returnActivity();
                        }
                    });
                    builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //pass
                        }
                    });

                    builder.show();
                }
                else{
                    returnActivity();
                }

            }
        });

    }
    public void returnActivity(){
        Intent intent = new Intent(TimerActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}