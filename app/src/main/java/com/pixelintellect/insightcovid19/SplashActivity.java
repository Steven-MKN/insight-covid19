package com.pixelintellect.insightcovid19;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.pixelintellect.insightcovid19.utils.Constants;
import com.pixelintellect.insightcovid19.utils.DataController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    private String TAG = "com.pixelintellect.insightcovid19.SplashActivity";
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i(TAG, "admob init = " + initializationStatus.toString());
            }
        });

        tvMessage = findViewById(R.id.textViewDownloadingData);
    }

    public void next(@Nullable String m){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (m != null)
            intent.putExtra(Constants.MESSAGE, m);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean updatesAvailable = getIntent().getBooleanExtra(Constants.UPDATES, false);

        //check if data exists
        SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String date = sp.getString(Constants.LAST_UPDATE, null);
        if (date == null || updatesAvailable){
            if (tvMessage != null) tvMessage.setVisibility(View.VISIBLE);

            BroadcastReceiver br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.i(TAG, "splash br got broadcast");
                    String m = intent.getStringExtra(Constants.MESSAGE);

                    next(m);
                }
            };
            IntentFilter intentFilter = new IntentFilter("afsdfs");
            getApplicationContext().registerReceiver(br, intentFilter);
            new DataController(getSharedPreferences(getPackageName(), MODE_PRIVATE)).updateData(getApplicationContext(), "afsdfs");

        } else {
            // up to date
            Log.i(TAG, "up to date");
            next(null);
        }

        Log.i(TAG, "SplashActivity");
    }
}
