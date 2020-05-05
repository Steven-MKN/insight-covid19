package com.pixelintellect.insight;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.pixelintellect.insight.utils.AppData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllTimeFragment extends Fragment {
    private AnyChartView barProvinceView;
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
        barProvinceView = view.findViewById(R.id.barChartAllTimePositives);
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
        // adjust chart sizes
        ViewGroup.LayoutParams params = barProvinceView.getLayoutParams();
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);

        params.height = point.x + 60;
        params.width = point.x - 15;

        barProvinceView.setLayoutParams(params);

        // display data
        List<DataEntry> dataEntries = new ArrayList<>();
        Map<String, String> provincalPositives = AppData.getInstance().getProvincialPositives();

        dataEntries.add(new ValueDataEntry("Gauteng", Integer.parseInt(provincalPositives.get("GP"))));
        dataEntries.add(new ValueDataEntry("Western Cape", Integer.parseInt(provincalPositives.get("WC"))));
        dataEntries.add(new ValueDataEntry("Kwa-Zulu Natal", Integer.parseInt(provincalPositives.get("KZN"))));
        dataEntries.add(new ValueDataEntry("North West", Integer.parseInt(provincalPositives.get("NW"))));
        dataEntries.add(new ValueDataEntry("Northern Cape", Integer.parseInt(provincalPositives.get("NC"))));
        dataEntries.add(new ValueDataEntry("Limpopo", Integer.parseInt(provincalPositives.get("LP"))));
        dataEntries.add(new ValueDataEntry("Mpumalanga", Integer.parseInt(provincalPositives.get("MP"))));
        dataEntries.add(new ValueDataEntry("Eastern Cape", Integer.parseInt(provincalPositives.get("EC"))));
        dataEntries.add(new ValueDataEntry("Free State", Integer.parseInt(provincalPositives.get("FS"))));

        Cartesian bar = AnyChart.column();
        bar.data(dataEntries);

        barProvinceView.setChart(bar);
        tvBarUpdateDate.setText(provincalPositives.get("date"));
    }
}
