package com.pixelintellect.insight;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.DataController;

public class SplashActivity extends AppCompatActivity {
    private String TAG = "SplashActivity";
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvMessage = findViewById(R.id.textViewDownloadingData);
    }

    /**
     * Goes to the next activity
     * @param m message, if any
     */
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

        // true when opened via the notification
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
                    context.unregisterReceiver(this);
                    next(m);
                }
            };

            IntentFilter intentFilter = new IntentFilter("afsdfs");
            getApplicationContext().registerReceiver(br, intentFilter);

            // requests data updates
            new DataController(getSharedPreferences(getPackageName(), MODE_PRIVATE)).updateData(getApplicationContext(), "afsdfs");

        } else {
            // up to date
            Log.i(TAG, "up to date");
            next(null);
        }

        Log.i(TAG, "SplashActivity");
    }
}
