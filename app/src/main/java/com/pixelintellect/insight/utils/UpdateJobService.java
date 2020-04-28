package com.pixelintellect.insight.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.reflect.TypeToken;
import com.pixelintellect.insight.R;
import com.pixelintellect.insight.SplashActivity;
import com.pixelintellect.insight.utils.models.ProvincialCumulativeConfirmedModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateJobService extends JobService {
    private final String TAG = "com.pixelintellect.insight.utils.CovidUpdateJobService";
    private final String baseCsvUrl = "https://raw.githubusercontent.com/dsfsi/covid19za/master/data/";
    private final String provincialConfirmedCasesCsvUrl = "covid19za_provincial_cumulative_timeline_confirmed.csv";

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.i(TAG, "requesting provincial confirmed cases csv...");
        String url = baseCsvUrl + provincialConfirmedCasesCsvUrl;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "error getting csv");
                e.printStackTrace();
                jobFinished(params, false);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "got csv");

                Type token = new TypeToken<ArrayList<ProvincialCumulativeConfirmedModel>>() {
                }.getType();
                ArrayList<ProvincialCumulativeConfirmedModel> confirmedCases = CsvMappers.mapProvincialConfirmedCasesCsv(response.body().string());

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String lastUpdateDateStr = getSharedPreferences(getPackageName(), MODE_PRIVATE).getString(Constants.LAST_UPDATE, null);
                    Date lastUpdateDate = simpleDateFormat.parse(lastUpdateDateStr);

                    Date dataDate = confirmedCases.get(confirmedCases.size() - 1).getDate();

                    if (lastUpdateDate != null && lastUpdateDate.before(dataDate)){
                        Log.i(TAG, "new data found");
                        // send notification
                        setNotification();
                    }
                    jobFinished(params, false);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    private void setNotification(){
        String CHANNEL_ID = getPackageName()+"notif_updates";
        int notificationId = 1;

        createNotificationChannel(CHANNEL_ID);

        // Create an explicit intent for activity
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra(Constants.UPDATES, true);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_trans)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("New updated data available")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

    }

    private void createNotificationChannel(String CHANNEL_ID) {
        // Create the NotificationChannel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Insight Data Updates";
            String description = "Updates on data in SA";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(getClass().getName(), "job cancelled before completion");
        return false;
    }
}
