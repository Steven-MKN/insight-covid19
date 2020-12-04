package com.pixelintellect.insight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.pixelintellect.insight.utils.AppData;
import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.Converters;
import com.pixelintellect.insight.utils.DataController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OneDayFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = "OneDayFragment";
    private TextView tvdeathsNumber, tvPositivesNumber, tvRecoveredNumber, tvTestsNumber, tvProvincesDate;
    private EditText etDate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppData appData;
    private HashMap<String, Date> dates;
    private SimpleDateFormat simpleDateFormat;
    private BarChart barView;

    /**
     * Creates and returns anew instance of this class
     * @return OneDayFragment
     */
    public static OneDayFragment newInstance() {
        Log.i("tag", "OneDayFragment");

        OneDayFragment instance = new OneDayFragment();
        instance.appData = AppData.getInstance();
        instance.simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_day, container, false);

        // init views
        tvdeathsNumber = view.findViewById(R.id.textViewDeathsNum);
        tvPositivesNumber = view.findViewById(R.id.textViewPositiveCasesNumber);
        tvRecoveredNumber = view.findViewById(R.id.textViewRecoveredNumber);
        tvTestsNumber = view.findViewById(R.id.textViewTestsNumber);
        tvProvincesDate = view.findViewById(R.id.textViewPieProvincesUpdateDate);
        etDate = view.findViewById(R.id.editTextDate);
        barView = view.findViewById(R.id.barViewOneDayProvince);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_one_day);

        // on click listener to allow the changing of dates
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // date of the current displayed data
                    Date showingDate = simpleDateFormat.parse(etDate.getText().toString());

                    DatePickerFragment datePickerFragment = new DatePickerFragment(
                            getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(year, month, dayOfMonth);

                                    setData(simpleDateFormat.format(calendar.getTime()));
                                }
                            },
                            showingDate,
                            dates.get("early"), // min selectable date
                            dates.get("late") // max selectable date
                    );

                    datePickerFragment.show(getChildFragmentManager(), "date_picker");

                } catch (ParseException e){
                    Log.i(TAG, "date parse error");
                    e.printStackTrace();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addDatesAndDisplayDefault();
    }

    private void addDatesAndDisplayDefault(){
        dates = appData.getDates();
        setData(simpleDateFormat.format(dates.get("late")));
    }

    private void setData(String date){
        tvdeathsNumber.setText(Converters.tallyToFormatString(appData.getDeathsOn(date)));
        tvPositivesNumber.setText(Converters.tallyToFormatString(appData.getPositiveCasesOn(date)));
        tvRecoveredNumber.setText(Converters.tallyToFormatString(appData.getRecoveredOn(date)));
        tvTestsNumber.setText(Converters.tallyToFormatString(appData.getTestsOn(date)));

        etDate.setText(date);

        setUpPieChart(date);
    }

    private void setUpPieChart(String d) {
        Map<String, String> provincalPositives = AppData.getInstance().getProvincialPositivesOn(d);
        tvProvincesDate.setText(provincalPositives.get("date"));

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

        Description descrip = new Description();
        descrip.setText("");
        barView.setDescription(descrip);
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
                addDatesAndDisplayDefault();
            }
        };
        IntentFilter intentFilter = new IntentFilter("sdoiahsac");
        getContext().registerReceiver(br, intentFilter);

        // attempts to retrieve updated data
        new DataController(
            getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE))
            .updateData(getContext(), "sdoiahsac");

    }

    public static class DatePickerFragment extends DialogFragment {
        private int year, month, day;
        private long minDate, maxDate;
        private Context fragCtx;
        private DatePickerDialog.OnDateSetListener listener;

        public DatePickerFragment(Context context, DatePickerDialog.OnDateSetListener listener, Date showingDate, Date minDate, Date maxDate){
            fragCtx = context;
            this.listener = listener;

            this.minDate = minDate.getTime();
            this.maxDate = maxDate.getTime();

            Calendar c = Calendar.getInstance();
            c.setTime(showingDate);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            DatePickerDialog dialog = new DatePickerDialog(fragCtx, listener, year, month, day);
            dialog.getDatePicker().setMinDate(minDate);
            dialog.getDatePicker().setMaxDate(maxDate);
            return dialog;
        }


    }

}
