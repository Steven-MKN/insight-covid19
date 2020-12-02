package com.pixelintellect.insight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.pixelintellect.insight.utils.AppData;
import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.Converters;
import com.pixelintellect.insight.utils.DataController;

import java.util.ArrayList;
import java.util.Map;


public class AllTimeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private TextView tvdeathsNumber, tvPositivesNumber, tvRecoveredNumber, tvTestsNumber, tvDate, tvBarUpdateDate;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Creates and returns anew instance of this class
     * @return AllTimeFragment
     */
    public static AllTimeFragment newInstance(){
        Log.i("tag", "OneDayFragment");
        return new AllTimeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_time, container, false);

        // init views
        tvdeathsNumber = view.findViewById(R.id.textViewTotalDeathsNumber);
        tvPositivesNumber = view.findViewById(R.id.textViewTotalPositiveCasesNumber);
        tvRecoveredNumber = view.findViewById(R.id.textViewTotalRecoveredNumber);
        tvTestsNumber = view.findViewById(R.id.textViewTotalTestsNumber);
        tvDate = view.findViewById(R.id.textViewDateAllTime);
        tvBarUpdateDate = view.findViewById(R.id.textViewBarAllTimeUpdateDate);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_all_time);

        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppData appData = AppData.getInstance();

        tvdeathsNumber.setText(Converters.tallyToFormatString(appData.getDeaths()));
        tvPositivesNumber.setText(Converters.tallyToFormatString(appData.getPositiveCases()));
        tvRecoveredNumber.setText(Converters.tallyToFormatString(appData.getRecovered()));
        tvTestsNumber.setText(Converters.tallyToFormatString(appData.getTests()));
        tvDate.setText(appData.getLatestDate());

        try {
            setUpBarChart();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpBarChart(){
        // display data
        Map<String, String> provincalPositives = AppData.getInstance().getProvincialPositives();
        BarChart barView = getView().findViewById(R.id.barChartAllTimePositives);
        barView.clear();

        BarData barData = new BarData();
        barData.addDataSet(makeDataSet(
            Constants.GAUTENG,
            Float.parseFloat(provincalPositives.get(Constants.GAUTENG)),
            Color.rgb(76,175,78),
            0
            )
        );
        barData.addDataSet(makeDataSet(
            Constants.WESTERN_CAPE,
            Float.parseFloat(provincalPositives.get(Constants.WESTERN_CAPE)),
            Color.rgb(33,148,243),
            1
            )
        );
        barData.addDataSet(makeDataSet(
            Constants.KWA_ZULU_NATAL,
            Float.parseFloat(provincalPositives.get(Constants.KWA_ZULU_NATAL)),
            Color.rgb(255,193,7),
            2
            )
        );
        barData.addDataSet(makeDataSet(
            Constants.EASTERN_CAPE,
            Float.parseFloat(provincalPositives.get(Constants.EASTERN_CAPE)),
            Color.rgb(244,67,54),
            3
            )
        );
        barData.addDataSet(makeDataSet(
            Constants.FREE_STATE,
            Float.parseFloat(provincalPositives.get(Constants.FREE_STATE)),
            Color.rgb(121,85,72),
            4
            )
        );
        barData.addDataSet(makeDataSet(
            Constants.LIMPOPO,
            Float.parseFloat(provincalPositives.get(Constants.LIMPOPO)),
            Color.rgb(104,58,183),
            5
            )
        );
        barData.addDataSet(makeDataSet(
            Constants.NORTH_WEST,
            Float.parseFloat(provincalPositives.get(Constants.NORTH_WEST)),
            Color.rgb(255,235,59),
            6
            )
        );
        barData.addDataSet(makeDataSet(
            Constants.MPUMALANGA,
            Float.parseFloat(provincalPositives.get(Constants.MPUMALANGA)),
            Color.rgb(233,30,98),
            7
            )
        );
        barData.addDataSet(makeDataSet(
            Constants.NORTHERN_CAPE,
            Float.parseFloat(provincalPositives.get(Constants.NORTHERN_CAPE)),
            Color.rgb(0,187,212),
            8
            )
        );

        if (!provincalPositives.get(Constants.UNKNOWN).equals("0"))
            barData.addDataSet(makeDataSet(
                Constants.UNKNOWN,
                Float.parseFloat(provincalPositives.get(Constants.UNKNOWN)),
                Color.rgb(96,125,139),
                9
                )
            );

        barView.setData(barData);

        Description d = new Description();
        d.setText("");
        barView.setDescription(d);

        tvBarUpdateDate.setText(provincalPositives.get(Constants.DATE));
    }

    public BarDataSet makeDataSet(String label, float value, int color, float index){
        ArrayList<BarEntry> arrayList = new ArrayList<BarEntry>();
        arrayList.add(new BarEntry(index, value));
        BarDataSet barDataSet = new BarDataSet(arrayList, label);
        barDataSet.setColor(color);

        return barDataSet;
    }

    @Override
    public void onRefresh() {
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);

                // removing the swipe refresh layout progress bar
                swipeRefreshLayout.setRefreshing(false);

                String message = intent.getStringExtra(Constants.MESSAGE);
                if (message == null) message = "Done!";

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                // update data on this fragment
                try {
                    setUpBarChart();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter("sdoiahsac");
        getContext().registerReceiver(br, intentFilter);

        // attempts to retrieve updated data
        new DataController(
            getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE))
            .updateData(getContext(), "sdoiahsac");

    }
}
