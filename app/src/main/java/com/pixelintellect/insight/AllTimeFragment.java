package com.pixelintellect.insight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pixelintellect.insight.utils.AppData;
import com.pixelintellect.insight.utils.Constants;
import java.util.ArrayList;
import java.util.Map;

import im.dacer.androidcharts.BarView;

public class AllTimeFragment extends Fragment {
    //private AnyChartView barProvinceView;
    private TextView tvdeathsNumber, tvPositivesNumber, tvRecoveredNumber, tvTestsNumber, tvDate, tvBarUpdateDate;

    public static AllTimeFragment newInstance(){
        Log.i("tag", "OneDayFragment");
        return new AllTimeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_time, container, false);

        //init views
        //barProvinceView = view.findViewById(R.id.barChartAllTimePositives);
        tvdeathsNumber = view.findViewById(R.id.textViewTotalDeathsNumber);
        tvPositivesNumber = view.findViewById(R.id.textViewTotalPositiveCasesNumber);
        tvRecoveredNumber = view.findViewById(R.id.textViewTotalRecoveredNumber);
        tvTestsNumber = view.findViewById(R.id.textViewTotalTestsNumber);
        tvDate = view.findViewById(R.id.textViewDateAllTime);
        tvBarUpdateDate = view.findViewById(R.id.textViewBarAllTimeUpdateDate);

        return view;
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppData appData = AppData.getInstance();

        tvdeathsNumber.setText(appData.getDeaths());
        tvPositivesNumber.setText(appData.getPositiveCases());
        tvRecoveredNumber.setText(appData.getRecovered());
        tvTestsNumber.setText(appData.getTests());
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
        ArrayList<Integer> dataEntries = new ArrayList<>();
        ArrayList provinces = new ArrayList();
        BarView barView = getView().findViewById(R.id.barChartAllTimePositives);

        provinces.add(Constants.GAUTENG + ": " + provincalPositives.get(Constants.GAUTENG));
        provinces.add(Constants.WESTERN_CAPE + ": " + provincalPositives.get(Constants.WESTERN_CAPE));
        provinces.add(Constants.KWA_ZULU_NATAL + ": " + provincalPositives.get(Constants.KWA_ZULU_NATAL));
        provinces.add(Constants.EASTERN_CAPE + ": " + provincalPositives.get(Constants.EASTERN_CAPE));
        provinces.add(Constants.FREE_STATE + ": " + provincalPositives.get(Constants.FREE_STATE));
        provinces.add(Constants.LIMPOPO + ": " + provincalPositives.get(Constants.LIMPOPO));
        provinces.add(Constants.NORTH_WEST + ": " + provincalPositives.get(Constants.NORTH_WEST));
        provinces.add(Constants.MPUMALANGA + ": " + provincalPositives.get(Constants.MPUMALANGA));
        provinces.add(Constants.NORTHERN_CAPE + ": " + provincalPositives.get(Constants.NORTHERN_CAPE));
        if (!provincalPositives.get(Constants.UNKNOWN).equals("0"))
            provinces.add(Constants.UNKNOWN + ": " + provincalPositives.get(Constants.UNKNOWN));

        barView.setBottomTextList(provinces);

        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.GAUTENG)));
        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.WESTERN_CAPE)));
        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.KWA_ZULU_NATAL)));
        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.NORTH_WEST)));
        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.NORTHERN_CAPE)));
        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.LIMPOPO)));
        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.MPUMALANGA)));
        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.EASTERN_CAPE)));
        dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.FREE_STATE)));
        if (!provincalPositives.get(Constants.UNKNOWN).equals("0"))
            dataEntries.add(Integer.parseInt(provincalPositives.get(Constants.UNKNOWN)));

        // find max
        int max = Integer.MIN_VALUE;
        for (int data : dataEntries) {
            if (data > max) max = data;
        }

        barView.setDataList(dataEntries, (int) (max * 1.3));
        tvBarUpdateDate.setText(provincalPositives.get(Constants.DATE));
    }
}
