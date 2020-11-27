package com.pixelintellect.insight.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.pixelintellect.insight.utils.models.DeathsModel;
import com.pixelintellect.insight.utils.models.ProvincialCumulativeConfirmedModel;
import com.pixelintellect.insight.utils.models.TestingTimelineModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CsvMappers {
    private static final String TAG = "CsvMappers";

    /**
     * Takes csv contents in String format and converts it into an ArrayList
     * @param s csv contents
     * @return ArrayList<DeathsModel>
     */
    public static @NonNull ArrayList<DeathsModel> mapDeathsCsv(String s){
        // this turns each line of the csv into a single element in the String array
        String lines[] = s.split("\n");

        // the expected date format from the csv
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        ArrayList<DeathsModel> deaths = new ArrayList<DeathsModel>();

        for (int i = 1; i < lines.length; i++){

            // for the current line, split text by ','
            String parts[] = lines[i].split(",");

            try {
                Date d = dateFormat.parse(parts[1]);
                String province = parts[3];

                deaths.add(new DeathsModel(d, province));

            } catch (ParseException e){
                Log.i(TAG, "date parse failed");
                e.printStackTrace();
            }
        }

        return deaths;
    }

    /**
     * Takes csv contents in String format and converts it into an ArrayList
     * @param s csv contents
     * @return ArrayList<ProvincialCumulativeConfirmedModel>
     */
    public static @NonNull ArrayList<ProvincialCumulativeConfirmedModel> mapProvincialConfirmedCasesCsv(String s){
        // this turns each line of the csv into a single element in the String array
        String lines[] = s.split("\n");

        // the expected date format from the csv
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        ArrayList<ProvincialCumulativeConfirmedModel> confirmedCases = new ArrayList<ProvincialCumulativeConfirmedModel>();

        for (int i = 1; i < lines.length; i++){

            // for the current line, split text by ','
            String parts[] = lines[i].split(",");

            try {
                Date d = dateFormat.parse(parts[0]);

                confirmedCases.add(new ProvincialCumulativeConfirmedModel(
                        d,
                        parts[2],
                        parts[4],
                        parts[10],
                        parts[8],
                        parts[3],
                        parts[6],
                        parts[5],
                        parts[9],
                        parts[7],
                        parts[11],
                        parts[12]));

            } catch (ParseException e){
                Log.i(TAG, "date parse failed");
                e.printStackTrace();
            }
        }

        return confirmedCases;
    }

    /**
     * Takes csv contents in String format and converts it into an ArrayList
     * @param s csv contents
     * @return ArrayList<TestingTimelineModel>
     */
    public static @NonNull ArrayList<TestingTimelineModel> mapTestingTimelineCsv(String s){
        // this turns each line of the csv into a single element in the String array
        String lines[] = s.split("\n");

        // the expected date format from the csv
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        ArrayList<TestingTimelineModel> tests = new ArrayList<TestingTimelineModel>();

        for (int i = 1; i < lines.length; i++){

            // for the current line, split text by ','
            String parts[] = lines[i].split(",");

            try {
                Date d = dateFormat.parse(parts[0]);

                tests.add(new TestingTimelineModel(d,
                        parts[2],
                        parts[3],
                        parts[7]));

            } catch (ParseException e){
                Log.i(TAG, "date parse failed");
                e.printStackTrace();
            }
        }

        return tests;
    }
}
