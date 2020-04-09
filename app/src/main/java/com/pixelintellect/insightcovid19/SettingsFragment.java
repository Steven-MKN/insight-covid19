package com.pixelintellect.insightcovid19;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pixelintellect.insightcovid19.utils.Constants;
import com.pixelintellect.insightcovid19.utils.CovidUpdateJobService;
import com.pixelintellect.insightcovid19.utils.DataController;
import com.pixelintellect.insightcovid19.utils.FragmentCnt;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private final String TAG = "com.pixelintellect.insightcovid19.SettingsFragment";
    private Button btnRefresh, btnPrivacyPolicy;
    private Switch switchNotifications;

    public static SettingsFragment newInstance(){
        Log.i("tag", "OneDayFragment");
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnPrivacyPolicy = view.findViewById(R.id.buttonPrivacyPolicy);
        btnRefresh = view.findViewById(R.id.buttonForceRefresh);
        switchNotifications = view.findViewById(R.id.switchNotifications);

        //
        JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (scheduler != null)
            if (scheduler.getPendingJob(123) != null)
                switchNotifications.setChecked(true);
            else switchNotifications.setChecked(false);

        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String url = "https://pixelintellect.co.za/apps/insight_covid19/privacy-policy.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setEnabled(false);
                BroadcastReceiver br = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        btnRefresh.setEnabled(true);
                        String message = intent.getStringExtra(Constants.MESSAGE);
                        if (message == null) message = "Done!";
                        alert(message);
                    }
                };
                IntentFilter intentFilter = new IntentFilter("sdoiahsac");
                getContext().registerReceiver(br, intentFilter);

                new DataController(
                        getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE))
                        .updateData(getContext(), "sdoiahsac");
            }
        });

        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ComponentName componentName = new ComponentName(getActivity().getApplicationContext(), CovidUpdateJobService.class);
                    JobInfo.Builder builder = new JobInfo.Builder(123, componentName)
                            .setPeriodic(15 * 60 * 1000);

                    builder.setPersisted(true);

                    JobInfo jobInfo = builder.build();

                    JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                    String message = "";
                    if (scheduler != null) {
                        int result = scheduler.schedule(jobInfo);
                        if (result == JobScheduler.RESULT_SUCCESS) {
                            message = "notifications enabled";
                        } else {
                            buttonView.setChecked(false);
                            message = "cannot schedule updates";
                        }
                    } else {
                        buttonView.setChecked(false);
                        message = "device not capable";
                    }
                    Log.i(TAG, message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                    if (scheduler != null) scheduler.cancel(123);
                    Log.i(TAG, "notifications disabled");
                    Toast.makeText(getContext(), "notifications disabled", Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            AdView adView = view.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void alert(String m){
        new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("Refresh")
                .setMessage(m)
                .setPositiveButton("Okay", null)
                .show();
    }

}
