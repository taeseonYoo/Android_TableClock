package com.taetae.table_clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    LinearLayout h_layout;
    NavigationView view1;
    DrawerLayout dl1;
    SeekBar textSize,brightLevel;
    View innerView,brightView,headerView;
    Menu menu;
    AlertDialog.Builder dlg, dlg2;
    TextClock tc_ap,tc_tm;
    ImageView ivBattery,myImage;
    Bitmap img;
    boolean flag;
    boolean check_p;
    int size_basic;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Resources r = Resources.getSystem();
        Configuration config = r.getConfiguration();
        if( config.orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            flag = false;
        }
        else {
            flag = true;
        }

        dl1 = findViewById(R.id.main_layout);
        view1 = findViewById(R.id.navigationView);
        h_layout = findViewById(R.id.height_layout);
        tc_ap = findViewById(R.id.clock_ap);
        tc_tm = findViewById(R.id.clock_time);
        innerView = getLayoutInflater().inflate(R.layout.seek_bar,null);
        dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setTitle("Choose textSize");
        dlg.setView(innerView);
        textSize = innerView.findViewById(R.id.SeekBar_tSize);
        textSize.setProgress(size_basic);
        check_p = false;
        ivBattery = findViewById(R.id.ivBattery);

        headerView = view1.getHeaderView(0);
        myImage = headerView.findViewById(R.id.iv_image);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(Intent.ACTION_PICK);
                 intent.setType("image/*");
                 intent.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(intent,REQUEST_CODE);


            }
        });




        h_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl1.openDrawer(view1);
            }
        });


        view1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_color:
                        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(MainActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        builder.setTitle("My ColorPickerDialog");
                        builder.setPreferenceName("MyColorPickerDialog");
                        builder.setPositiveButton("ok", new ColorListener() {
                            @Override
                            public void onColorSelected(int color, boolean fromUser) {
                                TextClock clock_ap = findViewById(R.id.clock_ap);
                                TextClock clock_time = findViewById(R.id.clock_time);
                                clock_ap.setTextColor(color);
                                clock_time.setTextColor(color);
                            }
                        });
                        builder.show();
                        return true;
                    case R.id.item_textSize:
                        dialogSeekbar();
                        return true;
                    case R.id.item_timer:
                        Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.item_record:
                        break;
                    case R.id.item_elim:
                        if(!check_p){
                            h_layout.removeView(tc_ap);
                            check_p = true;
                        }
                        else{
                            h_layout.removeView(tc_tm);
                            h_layout.addView(tc_ap);
                            h_layout.addView(tc_tm);
                            check_p = false;
                        }
                        break;
                    case R.id.item_bright:
                        dialogBright();
                        break;
                    case R.id.item_dark:
                        menu = view1.getMenu(); //네비게이션 뷰에 붙어있는 메뉴를 가져온다.
                        if(menu.findItem(R.id.item_dark).getTitle().equals("다크 모드")){ //현재 다크 모드
                            menu.findItem(R.id.item_dark).setTitle("라이트 모드");
                            h_layout.setBackgroundColor(Color.BLACK);
                            if(tc_ap.getCurrentTextColor()==Color.BLACK){
                                tc_ap.setTextColor(Color.WHITE);
                            }
                            if(tc_tm.getCurrentTextColor()==Color.BLACK){
                                tc_tm.setTextColor(Color.WHITE);
                            }
                        }
                        else{    //현재 라이트 모드 일때
                            menu.findItem(R.id.item_dark).setTitle("다크 모드");
                            h_layout.setBackgroundColor(Color.WHITE);
                            if(tc_ap.getCurrentTextColor()==Color.WHITE){
                                tc_ap.setTextColor(Color.BLACK);
                            }
                            if(tc_tm.getCurrentTextColor()==Color.WHITE){
                                tc_tm.setTextColor(Color.BLACK);
                            }
                        }
                        break;

                }
                return false;
            }
        });


    }
    public void dialogBright(){
        brightView = getLayoutInflater().inflate(R.layout.bright_seekbar,null);
        dlg2 = new AlertDialog.Builder(MainActivity.this);
        dlg2.setTitle("밝기 조절");
        dlg2.setView(brightView);
        brightLevel = brightView.findViewById(R.id.SeekBar_bright);

        brightLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                if(progress<10) progress = 10;
                else if(progress>255) progress = 255;
                params.screenBrightness = progress/(float)255;
                getWindow().setAttributes(params);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlg2.show();

    }

    public void dialogSeekbar(){
        innerView = getLayoutInflater().inflate(R.layout.seek_bar,null);
        dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setTitle("Choose textSize");
        dlg.setView(innerView);
        textSize = innerView.findViewById(R.id.SeekBar_tSize);
        if(flag== true){textSize.setMax(97);}
        else textSize.setMax(150);
        textSize.setProgress(size_basic);
        textSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                size_basic = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tc_tm.setTextSize(TypedValue.COMPLEX_UNIT_DIP,size_basic);
                tc_ap.setTextSize(TypedValue.COMPLEX_UNIT_DIP,size_basic-30);
                Toast.makeText(MainActivity.this, Integer.toString(size_basic), Toast.LENGTH_SHORT).show();
            }
        });
        dlg.show();


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        switch(newConfig.orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                if(size_basic>=98){
                    tc_tm.setTextSize(TypedValue.COMPLEX_UNIT_DIP,50);
                    tc_ap.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                    size_basic = 50;
                    textSize.setProgress(size_basic);
                }
                flag = true;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                flag = false;
                break;


        }


    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

            if(isCharging==true){ //if battery is charging, show on charging image.
                ivBattery.setImageResource(R.drawable.stat_sys_battery_charge_anim5);
            }
            else{  //Battery is not charging.
                if(action.equals(Intent.ACTION_BATTERY_CHANGED)){  //detect battery changing
                    int remain = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                    if(remain>=95) ivBattery.setImageResource(R.drawable.stat_sys_battery_100);
                    else if(remain>=80) ivBattery.setImageResource(R.drawable.stat_sys_battery_80);
                    else if(remain>=60) ivBattery.setImageResource(R.drawable.stat_sys_battery_60);
                    else if(remain>=40) ivBattery.setImageResource(R.drawable.stat_sys_battery_40);
                    else ivBattery.setImageResource(R.drawable.stat_sys_battery_20);

                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter iFi = new IntentFilter();
        iFi.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br,iFi);
        restoreState();
    }
    protected void saveState(){

        if(img != null) {
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 70, baos1);
            byte[] bytes1 = baos1.toByteArray();
            String temp1 = Base64.encodeToString(bytes1, Base64.DEFAULT);
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("imgSave", temp1);
            editor.commit();
        }

    }
    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        if(pref!=null && pref.contains("imgSave")){
            String temp2 = pref.getString("imgSave","");
            byte[] encodeByte1 = Base64.decode(temp2, Base64.DEFAULT);
            Bitmap bitmap2 = BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
            myImage.setImageBitmap(bitmap2);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    myImage.setImageBitmap(img);
                } catch (Exception e) {

                }
            }
        }

    }
}