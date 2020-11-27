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

    /**
     * This method retrieves the raw csv file from the API's raw content, then stored the json
     * formatted String in the shared preferences. It sends an intent with the given action when finished.
     * @param c context of the calling activity or application
     * @param action the intent to be broadcast
     */
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

                // creates a token/schema used to serialize the data to json format
                Type token = new TypeToken<ArrayList<ProvincialCumulativeConfirmedModel>>() {
                }.getType();

                // gets the response body as csv string and maps it to an array list
                ArrayList<ProvincialCumulativeConfirmedModel> confirmedCases = CsvMappers.mapProvincialConfirmedCasesCsv(response.body().string());

                // saves the data into the shared preferences as a json string
                sp.edit().putString(Constants.SP_CONFIRMED_CASES_ARRAY_LIST, new Gson().toJson(confirmedCases, token)).apply();

                c.sendBroadcast(new Intent(action));
            }
        });

    }

    /**
     * This method retrieves the raw csv file from the API's raw content, then stored the json
     * formatted String in the shared preferences. It sends an intent with the given action when finished.
     * @param c context of the calling activity or application
     * @param action the intent to be broadcast
     */
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

                // creates a token/schema used to serialize the data to json format
                Type token = new TypeToken<ArrayList<TestingTimelineModel>>() {
                }.getType();

                // gets the response body as csv string and maps it to an array list
                ArrayList<TestingTimelineModel> tests = CsvMappers.mapTestingTimelineCsv(response.body().string());

                // saves the data into the shared preferences as a json string
                sp.edit().putString(Constants.SP_TESTING_TIMELINE_ARRAY_LIST, new Gson().toJson(tests, token)).apply();

                c.sendBroadcast(new Intent(action));
            }
        });
    }

    /**
     * Retrieves the latest csv files from the API's raw content. It saves the json formatted data in
     * the shared preferences then sends an intent to notify that the process is complete.
     * @param context context of the calling activity or application
     * @param action the intent to be broadcast
     */
    public void updateData (final Context context, final String action) {

        // this receiver listens to retrieveConfirmedCasesProvincalCsv() and retrieveTestingTimelineCsv()
        // the once both methods are complete, sends a new intent to context
        BroadcastReceiver br = new BroadcastReceiver() {
            int count = 0;
            @Override
            public void onReceive(Context context, Intent intent) {
                count++;
                Log.i(TAG, "Count = " + count);

                // waits for both intents
                if (count > 1){

                    // new load data to AppData instance
                    if (sp != null) loadData();

                    Log.i(TAG, "Sending broadcast...");
                    context.sendBroadcast(new Intent(action));
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_CSV + action);
        context.registerReceiver(br, intentFilter);

        retrieveConfirmedCasesProvincalCsv(context, Constants.ACTION_CSV + action);
        retrieveTestingTimelineCsv(context, Constants.ACTION_CSV + action);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        Date date = new Date();

        Log.i(TAG, "last update: " + df.format(date));
        sp.edit().putString(Constants.LAST_UPDATE, df.format(date)).apply();
    }

    /**
     * Extracts stored data from the shared preferences and stores it in the current AppData instance
     */
    public void loadData(){
        AppData appData = AppData.getInstance();

        ArrayList<ProvincialCumulativeConfirmedModel> provincialCumulativeConfirmedModels = new ArrayList<>();
        ArrayList<TestingTimelineModel> testingTimelineModels = new ArrayList<>();

        // get data from shared preferences
        String casesStr = sp.getString(Constants.SP_CONFIRMED_CASES_ARRAY_LIST, null);
        String testingStr = sp.getString(Constants.SP_TESTING_TIMELINE_ARRAY_LIST, null);

        if (casesStr != null) {
            Log.i(TAG, casesStr);

            // create token/schema of the expected data structure
            Type token = new TypeToken<ArrayList<ProvincialCumulativeConfirmedModel>>() {}.getType();

            // deserialize data to ArrayList<ProvincialCumulativeConfirmedModel>
            provincialCumulativeConfirmedModels = new Gson().fromJson(casesStr, token);
        }
        if (testingStr != null) {
            Log.i(TAG, testingStr);

            // create token/schema of the expected data structure
            Type token = new TypeToken<ArrayList<TestingTimelineModel>>() {}.getType();

            // deserialize data to ArrayList<TestingTimelineModel>
            testingTimelineModels = new Gson().fromJson(testingStr, token);
        }

        appData.setProvincialCumulativeConfirms(provincialCumulativeConfirmedModels);
        appData.setTestingTimelines(testingTimelineModels);

    }
}
