package com.pixelintellect.insight.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixelintellect.insight.utils.models.ProvincialCumulativeConfirmedModel;
import com.pixelintellect.insight.utils.models.TestingTimelineModel;

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

public class DataController {
    private final String TAG = "DataController";
    private SharedPreferences sp;

    private final String baseCsvUrl = "https://raw.githubusercontent.com/dsfsi/covid19za/master/data/";
    private final String deathsCsvUrl = "covid19za_timeline_deaths.csv";
    private final String provincialConfirmedCasesCsvUrl = "covid19za_provincial_cumulative_timeline_confirmed.csv";
    private final String testingTimelineCsvUrl = "covid19za_timeline_testing.csv";


    public DataController(SharedPreferences sp){
        this.sp = sp;
    }

//    private void retrieveDeathsCsv() {
//        Log.i(TAG, "requesting deaths csv...");
//        String url = baseCsvUrl + deathsCsvUrl;
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        Response response;
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.i(TAG, "error getting csv");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                Log.i(TAG, "got csv");
//                ArrayList<DeathsModel> deaths = CsvMappers.mapDeathsCsv(response.body().string());
//
//                sp.edit().putString(Constants.SP_DEATHS_ARRAY_LIST, new Gson().toJson(deaths)).apply();
//            }
//        });
//    }

    private void retrieveConfirmedCasesProvincalCsv(final Context c, final String action) {
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
                Intent intent = new Intent(action).putExtra(Constants.MESSAGE, "No internet connection");
                c.sendBroadcast(intent);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "got csv");

                Type token = new TypeToken<ArrayList<ProvincialCumulativeConfirmedModel>>() {
                }.getType();
                ArrayList<ProvincialCumulativeConfirmedModel> confirmedCases = CsvMappers.mapProvincialConfirmedCasesCsv(response.body().string());

                sp.edit().putString(Constants.SP_CONFIRMED_CASES_ARRAY_LIST, new Gson().toJson(confirmedCases, token)).apply();
                c.sendBroadcast(new Intent(action));
            }
        });

    }

    private void retrieveTestingTimelineCsv(final Context c, final String action) {
        Log.i(TAG, "requesting testing timeline csv...");
        String url = baseCsvUrl +testingTimelineCsvUrl;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "error getting csv");
                e.printStackTrace();
                Intent intent = new Intent(action).putExtra(Constants.MESSAGE, "No internet connection");
                c.sendBroadcast(intent);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "got csv");

                Type token = new TypeToken<ArrayList<TestingTimelineModel>>() {
                }.getType();
                ArrayList<TestingTimelineModel> tests = CsvMappers.mapTestingTimelineCsv(response.body().string());

                sp.edit().putString(Constants.SP_TESTING_TIMELINE_ARRAY_LIST, new Gson().toJson(tests, token)).apply();
                c.sendBroadcast(new Intent(action));
            }
        });
    }

    public void updateData (final Context context, final String action) {

        BroadcastReceiver br = new BroadcastReceiver() {
            int count = 0;
            @Override
            public void onReceive(Context context, Intent intent) {
                count++;
                Log.i(TAG, "Count = " + count);

                if (count > 1){
                    if (sp != null) loadData();
                    Log.i(TAG, "Sending broadcast...");
                    context.sendBroadcast(new Intent(action));
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_CSV+action);
        context.registerReceiver(br, intentFilter);

        retrieveConfirmedCasesProvincalCsv(context, Constants.ACTION_CSV+action);
        retrieveTestingTimelineCsv(context, Constants.ACTION_CSV+action);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        Date date = new Date();
        Log.i(TAG, "last update: " + df.format(date));
        sp.edit().putString(Constants.LAST_UPDATE, df.format(date)).apply();
    }

    public void loadData(){
        AppData appData = AppData.getInstance();

        ArrayList<ProvincialCumulativeConfirmedModel> provincialCumulativeConfirmedModels = new ArrayList<>();
        ArrayList<TestingTimelineModel> testingTimelineModels = new ArrayList<>();

        String casesStr = sp.getString(Constants.SP_CONFIRMED_CASES_ARRAY_LIST, null);
        String testingStr = sp.getString(Constants.SP_TESTING_TIMELINE_ARRAY_LIST, null);

        if (casesStr != null) {
            Log.i(TAG, casesStr);
            Type token = new TypeToken<ArrayList<ProvincialCumulativeConfirmedModel>>() {}.getType();
            provincialCumulativeConfirmedModels = new Gson().fromJson(casesStr, token);
        }
        if (testingStr != null) {
            Log.i(TAG, testingStr);
            Type token = new TypeToken<ArrayList<TestingTimelineModel>>() {}.getType();
            testingTimelineModels = new Gson().fromJson(testingStr, token);
        }

        appData.setProvincialCumulativeConfirms(provincialCumulativeConfirmedModels);
        appData.setTestingTimelines(testingTimelineModels);

    }
}
