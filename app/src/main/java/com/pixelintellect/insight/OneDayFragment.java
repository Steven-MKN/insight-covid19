package com.pixelintellect.insight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.pixelintellect.insight.utils.AppData;
import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.models.DeathsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OneDayFragment extends Fragment {
    private String TAG = "com.pixelintellect.insight.OneDayFragment";
    private AnyChartView pieProvinceView;
    private TextView tvdeathsNumber, tvPositivesNumber, tvRecoveredNumber, tvTestsNumber, tvProvincesDate, tvDate;
    private AdView adView;

    public static OneDayFragment newInstance(){
        Log.i("tag", "OneDayFragment");
        return new OneDayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_day, container, false);

        pieProvinceView = view.findViewById(R.id.pieChartOneDayProvince);
        tvdeathsNumber = view.findViewById(R.id.textViewDeathsNum);
        tvPositivesNumber = view.findViewById(R.id.textViewPositiveCasesNumber);
        tvRecoveredNumber = view.findViewById(R.id.textViewRecoveredNumber);
        tvTestsNumber = view.findViewById(R.id.textViewTestsNumber);
        tvProvincesDate = view.findViewById(R.id.textViewPieProvincesUpdateDate);
        tvDate = view.findViewById(R.id.textViewLatestDate);
        adView = view.findViewById(R.id.adView1);

        return view;
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppData appData = AppData.getInstance();

        tvdeathsNumber.setText(appData.getLatestDeaths());
        tvPositivesNumber.setText(appData.getLatestPositiveCases());
        tvRecoveredNumber.setText(appData.getLatestRecovered());
        tvTestsNumber.setText(appData.getLatestTests());
        tvDate.setText(appData.getLatestDate());

        setUpPieChart();

        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpPieChart(){
        // adjust chart sizes
        ViewGroup.LayoutParams params = pieProvinceView.getLayoutParams();
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);

        params.height = point.x;
        params.width = point.x - 15;

        pieProvinceView.setLayoutParams(params);

        // display data
        List<DataEntry> dataEntries = new ArrayList<>();
        Map<String, String> provincalPositives = AppData.getInstance().getLatestProvincalPositives();

        dataEntries.add(new ValueDataEntry("Gauteng", Integer.parseInt(provincalPositives.get("GP"))));
        dataEntries.add(new ValueDataEntry("Western Cape", Integer.parseInt(provincalPositives.get("WC"))));
        dataEntries.add(new ValueDataEntry("Kwa-Zulu Natal", Integer.parseInt(provincalPositives.get("KZN"))));
        dataEntries.add(new ValueDataEntry("North West", Integer.parseInt(provincalPositives.get("NW"))));
        dataEntries.add(new ValueDataEntry("Northern Cape", Integer.parseInt(provincalPositives.get("NC"))));
        dataEntries.add(new ValueDataEntry("Limpopo", Integer.parseInt(provincalPositives.get("LP"))));
        dataEntries.add(new ValueDataEntry("Mpumalanga", Integer.parseInt(provincalPositives.get("MP"))));
        dataEntries.add(new ValueDataEntry("Eastern Cape", Integer.parseInt(provincalPositives.get("EC"))));
        dataEntries.add(new ValueDataEntry("Free State", Integer.parseInt(provincalPositives.get("FS"))));

        Pie pie = AnyChart.pie();
        pie.data(dataEntries);

        pieProvinceView.setChart(pie);
        tvProvincesDate.setText(provincalPositives.get("date"));
    }
}
