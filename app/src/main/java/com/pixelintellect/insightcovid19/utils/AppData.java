package com.pixelintellect.insightcovid19.utils;

import android.content.Intent;

import com.pixelintellect.insightcovid19.utils.models.ProvincialCumulativeConfirmedModel;
import com.pixelintellect.insightcovid19.utils.models.TestingTimelineModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class AppData {
    private static AppData instance;
    private ArrayList<ProvincialCumulativeConfirmedModel> provincialCumulativeConfirms;
    private ArrayList<TestingTimelineModel> testingTimelines;

    private AppData(){
        provincialCumulativeConfirms = new ArrayList<>();
        testingTimelines = new ArrayList<>();
    }

    public static AppData getInstance(){
        if (instance == null)
            instance = new AppData();
        return instance;
    }

    public void setProvincialCumulativeConfirms(ArrayList<ProvincialCumulativeConfirmedModel> provincialCumulativeConfirms) {
        this.provincialCumulativeConfirms = provincialCumulativeConfirms;
    }

    public void setTestingTimelines(ArrayList<TestingTimelineModel> testingTimelines) {
        this.testingTimelines = testingTimelines;
    }

    public String getLatestDate(){
        try {
            ProvincialCumulativeConfirmedModel model = provincialCumulativeConfirms.get(provincialCumulativeConfirms.size() - 1);

            return new SimpleDateFormat("dd-MM-yyyy").format(model.getDate());
        } catch (Exception e){
            return "";
        }
    }

    public String getLatestPositiveCases(){
        int x = provincialCumulativeConfirms.size() - 1;
        int y = provincialCumulativeConfirms.size() - 2;
        if (x >= 0 && y >= 0) {
            ProvincialCumulativeConfirmedModel model = provincialCumulativeConfirms.get(x);
            ProvincialCumulativeConfirmedModel model2 = provincialCumulativeConfirms.get(y);

            if (isDigit(model.getTotal()) && isDigit(model2.getTotal())) {
                int todaysCases = Integer.parseInt(model.getTotal()) - Integer.parseInt(model2.getTotal());
                return String.valueOf(todaysCases);
            } else {
                return "-";
            }
        }else {
            return "-";
        }
    }

    public String getLatestRecovered(){
        int x = testingTimelines.size() - 1;
        int y = testingTimelines.size() - 2;
        if (x >= 0 && y >= 0) {
            TestingTimelineModel model = testingTimelines.get(x);
            TestingTimelineModel model2 = testingTimelines.get(y);

            if (isDigit(model.getDayRecovered()) && isDigit(model2.getDayRecovered())) {
                int todaysRecoveries = Integer.parseInt(model.getDayRecovered()) - Integer.parseInt(model2.getDayRecovered());
                return String.valueOf(todaysRecoveries);
            } else {
                return "-";
            }
        } else {
            return "-";
        }
    }

    public String getLatestDeaths(){
        int x = testingTimelines.size() - 1;
        int y = testingTimelines.size() - 2;
        if (x >= 0 && y >= 0){
            TestingTimelineModel model = testingTimelines.get(x);
            TestingTimelineModel model2 = testingTimelines.get(y);

            if (isDigit(model.getCumulativeDeaths()) && isDigit(model2.getCumulativeDeaths())) {
                int todaysDeaths = Integer.parseInt(model.getCumulativeDeaths()) - Integer.parseInt(model2.getCumulativeDeaths());
                return String.valueOf(todaysDeaths);
            } else {
                return "-";
            }
        } else {
            return "-";
        }
    }

    public String getLatestTests(){
        int x = testingTimelines.size() - 1;
        int y = testingTimelines.size() - 2;
        if (x >= 0 && y >= 0) {
            TestingTimelineModel model = testingTimelines.get(x);
            TestingTimelineModel model2 = testingTimelines.get(y);

            if (isDigit(model.getCumulativeTests()) && isDigit(model2.getCumulativeTests())) {
                int todaysTests = Integer.parseInt(model.getCumulativeTests()) - Integer.parseInt(model2.getCumulativeTests());
                return String.valueOf(todaysTests);
            } else {
                return "-";
            }
        } else {
            return "-";
        }
    }

    public HashMap<String, String> getLatestProvincalPositives(){
        ProvincialCumulativeConfirmedModel model = null, model2 = null;
        HashMap<String, String> map = new HashMap<String, String>();
        String gp, wc, kzn, lp, nw, nc, fs, ec, mp;
        try {
            int x = provincialCumulativeConfirms.size() - 1, y = provincialCumulativeConfirms.size() - 2;

            do {
                model = provincialCumulativeConfirms.get(x);
                model2 = provincialCumulativeConfirms.get(y);

                if (isDigit(model.getGp()) && isDigit(model2.getGp()))
                    gp = String.valueOf(Integer.parseInt(model.getGp()) - Integer.parseInt(model2.getGp()));
                else gp = "0";

                if (isDigit(model.getWc()) && isDigit(model2.getWc()))
                    wc = String.valueOf(Integer.parseInt(model.getWc()) - Integer.parseInt(model2.getWc()));
                else wc = "0";

                if (isDigit(model.getKzn()) && isDigit(model2.getKzn()))
                    kzn = String.valueOf(Integer.parseInt(model.getKzn()) - Integer.parseInt(model2.getKzn()));
                else kzn = "0";

                if (isDigit(model.getLp()) && isDigit(model2.getLp()))
                    lp = String.valueOf(Integer.parseInt(model.getLp()) - Integer.parseInt(model2.getLp()));
                else lp = "0";

                if (isDigit(model.getNw()) && isDigit(model2.getNw()))
                    nw = String.valueOf(Integer.parseInt(model.getNw()) - Integer.parseInt(model2.getNw()));
                else nw = "0";

                if (isDigit(model.getNc()) && isDigit(model2.getNc()))
                    nc = String.valueOf(Integer.parseInt(model.getNc()) - Integer.parseInt(model2.getNc()));
                else nc = "0";

                if (isDigit(model.getFs()) && isDigit(model2.getFs()))
                    fs = String.valueOf(Integer.parseInt(model.getFs()) - Integer.parseInt(model2.getFs()));
                else fs = "0";

                if (isDigit(model.getEc()) && isDigit(model2.getEc()))
                    ec = String.valueOf(Integer.parseInt(model.getEc()) - Integer.parseInt(model2.getEc()));
                else ec = "0";

                if (isDigit(model.getMp()) && isDigit(model2.getMp()))
                    mp = String.valueOf(Integer.parseInt(model.getMp()) - Integer.parseInt(model2.getMp()));
                else mp = "0";

                x--;
                y--;
                if (y < 0) break;

            } while (allZero(gp, wc, kzn, lp, nw, nc, fs, ec, mp));
        } catch (Exception e){
            gp = wc = kzn = lp = nw = nc = ec = fs = mp = "0";
        }

        String date;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").format(model.getDate());
        } catch (Exception e){
            date = "";
        }

        map.put("GP", gp);
        map.put("WC", wc);
        map.put("KZN", kzn);
        map.put("LP", lp);
        map.put("NW", nw);
        map.put("NC", nc);
        map.put("FS", fs);
        map.put("EC", ec);
        map.put("MP", mp);
        map.put("date", date);

        return map;
    }



    public HashMap<String, String> getProvincialPositives(){
        ProvincialCumulativeConfirmedModel model = null;
        HashMap<String, String> map = new HashMap<String, String>();
        int x = provincialCumulativeConfirms.size() - 1;
        String gp, wc, kzn, lp, nw, nc, fs, ec, mp;

        try {
            do {
                model = provincialCumulativeConfirms.get(x);

                gp = model.getGp().equals("") ? "0" : model.getGp();
                wc = model.getWc().equals("") ? "0" : model.getWc();
                kzn = model.getKzn().equals("") ? "0" : model.getKzn();
                fs = model.getFs().equals("") ? "0" : model.getFs();
                mp = model.getMp().equals("") ? "0" : model.getMp();
                ec = model.getEc().equals("") ? "0" : model.getEc();
                nc = model.getNc().equals("") ? "0" : model.getNc();
                nw = model.getNw().equals("") ? "0" : model.getNw();
                lp = model.getLp().equals("") ? "0" : model.getLp();

                x--;
                if (x < 0) break;
            } while (allZero(gp, wc, mp, ec, nc, nw, lp, kzn, fs));
        }catch (Exception e){
            gp = wc = kzn = lp = nw = nc = ec = fs = mp = "0";
        }
        String date;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").format(model.getDate());
        } catch (Exception e){
            date = "";
        }

        map.put("GP", gp);
        map.put("WC", wc);
        map.put("KZN", kzn);
        map.put("LP", lp);
        map.put("NW", nw);
        map.put("NC", nc);
        map.put("FS", fs);
        map.put("EC", ec);
        map.put("MP", mp);
        map.put("date", date);

        return map;
    }

    public String getPositiveCases(){
        ProvincialCumulativeConfirmedModel model;
        int x = provincialCumulativeConfirms.size() - 1;

        try {
            do {
                model = provincialCumulativeConfirms.get(x);
                x--;
            } while (!isDigit(model.getTotal()));

            return String.valueOf(model.getTotal());
        } catch (Exception e){
            return "-";
        }
    }

    public String getRecovered(){
        TestingTimelineModel model;
        try {
            int x = testingTimelines.size() - 1;
            do {
                model = testingTimelines.get(x);
                x--;
            } while (!isDigit(model.getDayRecovered()));

            return String.valueOf(model.getDayRecovered());
        } catch (Exception e){
            return "-";
        }
    }

    public String getDeaths(){
        TestingTimelineModel model;
        try {
            int x = testingTimelines.size() - 1;

            do {
                model = testingTimelines.get(x);
                x--;
            } while (!isDigit(model.getCumulativeDeaths()));

            return String.valueOf(model.getCumulativeDeaths());
        } catch (Exception e){
            return "-";
        }
    }

    public String getTests(){
        TestingTimelineModel model;
        try {
            int x = testingTimelines.size() - 1;

            do {
                model = testingTimelines.get(x);
                x--;
            } while (!isDigit(model.getCumulativeTests()));

            return String.valueOf(model.getCumulativeTests());
        } catch (Exception e){
            return "-";
        }
    }

    private boolean isDigit(String s){
        boolean isValid = true;

        if (s.equals("") ) isValid = false;

        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c))
                isValid = false;
        }

        return isValid;
    }

    private boolean allZero(String ...s){
        boolean allZero = true;
        for (String each : s){
            if (!each.equals("0"))
                allZero = false;
        }

        return allZero;
    }
}
