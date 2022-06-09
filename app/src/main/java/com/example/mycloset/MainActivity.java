package com.example.mycloset;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.mycloset.add.AddFragment;
import com.example.mycloset.coordi.CoordiFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;
    Toolbar myToolbar;
    private final int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        SettingListener();
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        bottomNavigationView.setSelectedItemId(R.id.tab_cody);
    }


    private void init(){
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener(){
        bottomNavigationView.setOnItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
            switch (menuItem.getItemId()){
                case R.id.tab_cody:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new CoordiFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_closet:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new ClosetFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_add: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new AddFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_weather:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new WeatherFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_setting:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new CommunityFragment())
                            .commit();
                    return true;
                }
            }
            return false;
        }
    }

}