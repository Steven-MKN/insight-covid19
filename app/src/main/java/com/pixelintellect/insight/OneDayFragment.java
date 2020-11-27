package com.pixelintellect.insight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.pixelintellect.insight.utils.AppData;
import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.Converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import im.dacer.androidcharts.BarView;

public class OneDayFragment extends Fragment {
    private String TAG = "OneDayFragment";
    private TextView tvdeathsNumber, tvPositivesNumber, tvRecoveredNumber, tvTestsNumber, tvProvincesDate;
    private EditText etDate;
    private AppData appData;
    private HashMap<String, Date> dates;
    private SimpleDateFormat simpleDateFormat;
    private BarView barView;

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

        tvdeathsNumber = view.findViewById(R.id.textViewDeathsNum);
        tvPositivesNumber = view.findViewById(R.id.textViewPositiveCasesNumber);
        tvRecoveredNumber = view.findViewById(R.id.textViewRecoveredNumber);
        tvTestsNumber = view.findViewById(R.id.textViewTestsNumber);
        tvProvincesDate = view.findViewById(R.id.textViewPieProvincesUpdateDate);
        etDate = view.findViewById(R.id.editTextDate);
        barView = view.findViewById(R.id.barViewOneDayProvince);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                            dates.get("early"),
                            dates.get("late")
                    );
                    datePickerFragment.show(getFragmentManager(), "date_picker");
                } catch (ParseException e){
                    Log.i(TAG, "date parse error");
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // add dates
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

        ArrayList provinces = new ArrayList();
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

        ArrayList<Integer> cases = new ArrayList();
        cases.add(Integer.parseInt(provincalPositives.get(Constants.GAUTENG)));
        cases.add(Integer.parseInt(provincalPositives.get(Constants.WESTERN_CAPE)));
        cases.add(Integer.parseInt(provincalPositives.get(Constants.KWA_ZULU_NATAL)));
        cases.add(Integer.parseInt(provincalPositives.get(Constants.EASTERN_CAPE)));
        cases.add(Integer.parseInt(provincalPositives.get(Constants.FREE_STATE)));
        cases.add(Integer.parseInt(provincalPositives.get(Constants.LIMPOPO)));
        cases.add(Integer.parseInt(provincalPositives.get(Constants.NORTH_WEST)));
        cases.add(Integer.parseInt(provincalPositives.get(Constants.MPUMALANGA)));
        cases.add(Integer.parseInt(provincalPositives.get(Constants.NORTHERN_CAPE)));
        if (!provincalPositives.get(Constants.UNKNOWN).equals("0"))
            cases.add(Integer.parseInt(provincalPositives.get(Constants.UNKNOWN)));

        // find max
        int max = Integer.MIN_VALUE;
        for (int _case : cases) {
            if (_case > max) max = _case;
        }

        barView.setDataList(cases, (int) (max * 1.3));
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
