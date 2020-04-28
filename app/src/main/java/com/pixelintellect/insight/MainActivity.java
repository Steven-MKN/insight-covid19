package com.pixelintellect.insight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.DataController;
import com.pixelintellect.insight.utils.FragmentCnt;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private final String TAG = "com.pixelintellect.insight.MainActivity";
    private BottomNavigationView bottomNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load data
        new DataController(getSharedPreferences(getPackageName(), MODE_PRIVATE)).loadData();

        // init views
        bottomNavigationMenu = (BottomNavigationView) findViewById(R.id.navigation_bar);
        bottomNavigationMenu.setOnNavigationItemSelectedListener(this);


        Log.i(TAG, "MainActivity");
        FragmentCnt.to(R.id.home_frag_holder, OneDayFragment.newInstance(), getSupportFragmentManager(), false, true);

        // check for messages
        String message = getIntent().getStringExtra(Constants.MESSAGE);
        if (message != null) {
            new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_one_day:
                FragmentCnt.to(R.id.home_frag_holder, OneDayFragment.newInstance(), getSupportFragmentManager(), false, true);
                break;
            case R.id.nav_all_time:
                FragmentCnt.to(R.id.home_frag_holder, AllTimeFragment.newInstance(), getSupportFragmentManager(), false, true);
                break;
            case R.id.nav_settings:
                FragmentCnt.to(R.id.home_frag_holder, SettingsFragment.newInstance(), getSupportFragmentManager(), false, true);
                break;
        }

        return true;
    }

}