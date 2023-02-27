package com.taetae.table_clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorListener;

public class MainActivity extends AppCompatActivity {
    LinearLayout h_layout;
    NavigationView view1;
    DrawerLayout dl1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dl1 = findViewById(R.id.main_layout);
        view1 = findViewById(R.id.navigationView);
        h_layout = findViewById(R.id.height_layout);


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
                    case R.id.item_timer:
                    case R.id.item_textSize:
                    case R.id.item_record:

                }
                return false;
            }
        });



    }


}