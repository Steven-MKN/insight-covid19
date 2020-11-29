package com.pixelintellect.insight;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.DataController;
import com.pixelintellect.insight.utils.UpdateJobService;

import java.util.List;

public class SettingsFragment extends Fragment {
    private final String TAG = "SettingsFragment";
    private Button btnPrivacyPolicy;
    private Switch switchNotifications;

    /**
     * Creates and returns anew instance of this class
     * @return SettingsFragment
     */
    public static SettingsFragment newInstance(){
        Log.i("tag", "OneDayFragment");
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // init views
        btnPrivacyPolicy = view.findViewById(R.id.buttonPrivacyPolicy);
        switchNotifications = view.findViewById(R.id.switchNotifications);

        // used to run background task to check for updates when notifications are enabled
        JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (scheduler != null){
            if (Build.VERSION.SDK_INT >= 24) {

                // if job is scheduled, set notifications as enabled
                if (scheduler.getPendingJob(123) != null)
                    switchNotifications.setChecked(true);
                else switchNotifications.setChecked(false);

            } else {

                // if job is scheduled, set notifications as enabled
                List<JobInfo> jobs = scheduler.getAllPendingJobs();
                boolean exists = false;
                for (JobInfo job: jobs){
                    if (job.getId() == 123) exists = true;
                }
                if (exists) switchNotifications.setChecked(true);
                else switchNotifications.setChecked(false);
            }
        }

        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String url = "https://stevenmokoena.co.za/insight/privacy-policy.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            }
        });

        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // used to run background task to check for updates when notifications are enabled

                    ComponentName componentName = new ComponentName(getActivity().getApplicationContext(), UpdateJobService.class);
                    JobInfo.Builder builder = new JobInfo.Builder(123, componentName)
                            .setPeriodic(60 * 60 * 1000);
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
                    // cancel job service (cancel checking for updates)
                    JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                    if (scheduler != null) scheduler.cancel(123);
                    Log.i(TAG, "notifications disabled");
                    Toast.makeText(getContext(), "notifications disabled", Toast.LENGTH_LONG).show();
                }
            }
        });

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
