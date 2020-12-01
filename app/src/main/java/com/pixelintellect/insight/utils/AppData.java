package com.pixelintellect.insight.utils;

/**
 * This class is a singleton, it contains all the temporary (non persisting) data that the app makes use of.
 * calling the static method getInstance() will create an instance of no instance exists and will return the instance.
 * That is to say, for the whole life cycle of the application will use a single instance of this class.
 */

import androidx.annotation.NonNull;

import com.pixelintellect.insight.utils.models.ArticlesModel;
import com.pixelintellect.insight.utils.models.NewsModel;
import com.pixelintellect.insight.utils.models.ProvincialCumulativeConfirmedModel;
import com.pixelintellect.insight.utils.models.TestingTimelineModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AppData {
    private static AppData instance;
    private ArrayList<ProvincialCumulativeConfirmedModel> provincialCumulativeConfirms;
    private ArrayList<TestingTimelineModel> testingTimelines;
    private NewsModel newsModel;

    /**
     * The constructor is private to prevent instantiation out side of this class, use getInstance()
     */
    private AppData(){
        provincialCumulativeConfirms = new ArrayList<>();
        testingTimelines = new ArrayList<>();
        newsModel = new NewsModel();
        newsModel.setArticles(new ArrayList<ArticlesModel>()); // to avoid null pointer errors
        newsModel.setStatus("");
        newsModel.setTotalResults("0");
    }

    /**
     * Returns an instance of the class, creates one if non exists
     * @return AppData
     */
    public static AppData getInstance(){
        if (instance == null)
            instance = new AppData();
        return instance;
    }

    /**
     * @param provincialCumulativeConfirms ArrayList<ProvincialCumulativeConfirmedModel>
     */
    public void setProvincialCumulativeConfirms(ArrayList<ProvincialCumulativeConfirmedModel> provincialCumulativeConfirms) {
        this.provincialCumulativeConfirms = provincialCumulativeConfirms;
    }

    /**
     *
     * @param testingTimelines ArrayList<TestingTimelineModel>
     */
    public void setTestingTimelines(ArrayList<TestingTimelineModel> testingTimelines) {
        this.testingTimelines = testingTimelines;
    }

    /**
     * Returns the date of the latest available data
     * @return String
     */
    public String getLatestDate(){
        try {
            ProvincialCumulativeConfirmedModel model = provincialCumulativeConfirms.get(provincialCumulativeConfirms.size() - 1);

            return new SimpleDateFormat("dd-MM-yyyy").format(model.getDate());
        } catch (Exception e){
            return "";
        }
    }

    /**
     * Returns the dates of all the available data
     * @return String
     */
    public HashMap<String, Date> getDates(){
        HashMap<String, Date> dateMap = new HashMap<String, Date>();

        if (provincialCumulativeConfirms.size() > 0){
            dateMap.put("late", provincialCumulativeConfirms.get(provincialCumulativeConfirms.size() - 1).getDate());
            dateMap.put("early", provincialCumulativeConfirms.get(0).getDate());
        } else {
            dateMap.put("early", Calendar.getInstance().getTime());
            dateMap.put("late", Calendar.getInstance().getTime());
        }

        return dateMap;
    }

    /**
     *  Get the number of positive cases on a specific date
     * @param date Date in String format
     * @return String
     */
    public String getPositiveCasesOn(String date){
        // this method is expensive, optimize soon!!
        String cases = "-";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int foundAt = -1;
        for (int i = 0; i < provincialCumulativeConfirms.size(); i++){
            String d = dateFormat.format(provincialCumulativeConfirms.get(i).getDate());
            if (date.equals(d)){
                foundAt = i;
                break;
            }
        }

        if (foundAt == 0){
            // this is first record, no calculations needed
            cases = provincialCumulativeConfirms.get(foundAt).getTotal();
        } else if (foundAt > 0) {
            // today's total less yesterday's total equals today's positive cases
            int x = foundAt;
            int y = foundAt - 1;

            ProvincialCumulativeConfirmedModel model = provincialCumulativeConfirms.get(x);
            ProvincialCumulativeConfirmedModel model2 = provincialCumulativeConfirms.get(y);

            if (isDigit(model.getTotal()) && isDigit(model2.getTotal())) {
                int todaysCases = Integer.parseInt(model.getTotal()) - Integer.parseInt(model2.getTotal());
                cases = String.valueOf(todaysCases);
            }
        }

        return cases;
    }

    /**
     *  Gets the number of recovered patients on a specific date
     * @param date Date in String format
     * @return String
     */
    public String getRecoveredOn(String date){
        // this method is expensive, optimize soon!!
        String recovered = "-";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int foundAt = -1;
        for (int i = 0; i < testingTimelines.size(); i++){
            String d = dateFormat.format(testingTimelines.get(i).getDate());
            if (date.equals(d)){
                foundAt = i;
                break;
            }
        }

        if (foundAt == 0){
            // this is first record, no calculations needed
            recovered = testingTimelines.get(foundAt).getDayRecovered();
        } else if (foundAt > 0) {
            // today's total less yesterday's total equals today's recoveries
            int x = foundAt;
            int y = foundAt - 1;

            TestingTimelineModel model = testingTimelines.get(x);
            TestingTimelineModel model2 = testingTimelines.get(y);

            if (isDigit(model.getDayRecovered()) && isDigit(model2.getDayRecovered())) {
                int todaysRecoveries = Integer.parseInt(model.getDayRecovered()) - Integer.parseInt(model2.getDayRecovered());
                recovered = String.valueOf(todaysRecoveries);
            }
        }

        return recovered;
    }

    /**
     *  Get the number of deaths on a specific date
     * @param date Date in String format
     * @return String
     */
    public String getDeathsOn(String date){
        // this method is expensive, optimize soon!!
        String deaths = "-";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int foundAt = -1;
        for (int i = 0; i < testingTimelines.size(); i++){
            String d = dateFormat.format(testingTimelines.get(i).getDate());
            if (date.equals(d)){
                foundAt = i;
                break;
            }
        }

        if (foundAt == 0){
            // this is first record, no calculations needed
            deaths = testingTimelines.get(foundAt).getCumulativeDeaths();
        } else if (foundAt > 0){
            // today's total less yesterday's total equals today's deaths
            int x = foundAt;
            int y = foundAt - 1;

            TestingTimelineModel model = testingTimelines.get(x);
            TestingTimelineModel model2 = testingTimelines.get(y);

            if (isDigit(model.getCumulativeDeaths()) && isDigit(model2.getCumulativeDeaths())) {
                int todaysDeaths = Integer.parseInt(model.getCumulativeDeaths()) - Integer.parseInt(model2.getCumulativeDeaths());
                deaths = String.valueOf(todaysDeaths);
            }
        }

        return deaths;
    }

    /**
     *  Get the number of tests completed on a specific date
     * @param date Date in String format
     * @return String
     */
    public String getTestsOn(String date){
        // this method is expensive, optimize soon!!
        String tests = "-";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int foundAt = -1;
        for (int i = 0; i < testingTimelines.size(); i++){
            String d = dateFormat.format(testingTimelines.get(i).getDate());
            if (date.equals(d)){
                foundAt = i;
                break;
            }
        }

        if (foundAt == 0){
            // this is first record, no calculations needed
            tests = testingTimelines.get(foundAt).getCumulativeTests();
        } else if (foundAt > 0) {
            // today's total less yesterday's total equals today's tests
            int x = foundAt;
            int y = foundAt - 1;

            TestingTimelineModel model = testingTimelines.get(x);
            TestingTimelineModel model2 = testingTimelines.get(y);

            if (isDigit(model.getCumulativeTests()) && isDigit(model2.getCumulativeTests())) {
                int todaysTests = Integer.parseInt(model.getCumulativeTests()) - Integer.parseInt(model2.getCumulativeTests());
                tests = String.valueOf(todaysTests);
            }
        }

        return tests;
    }

    /**
     * Gets positive cases for each province on a specific date
     * @param date Date in String format
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getProvincialPositivesOn(String date){
        // this method is expensive, optimize soon!!
        String tests = "-";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int foundAt = -1;
        for (int i = 0; i < provincialCumulativeConfirms.size(); i++){
            String d = dateFormat.format(provincialCumulativeConfirms.get(i).getDate());
            if (date.equals(d)){
                foundAt = i;
                break;
            }
        }

        HashMap<String, String> map = new HashMap<String, String>();
        String gp = "0", wc = "0", kzn = "0", lp = "0", nw = "0", nc = "0", fs = "0", ec = "0", mp = "0", unknown = "0";

        if (foundAt == 0){
            gp = provincialCumulativeConfirms.get(foundAt).getGp();
            wc = provincialCumulativeConfirms.get(foundAt).getWc();
            kzn = provincialCumulativeConfirms.get(foundAt).getKzn();
            nc = provincialCumulativeConfirms.get(foundAt).getNc();
            nw = provincialCumulativeConfirms.get(foundAt).getNw();
            lp = provincialCumulativeConfirms.get(foundAt).getLp();
            mp = provincialCumulativeConfirms.get(foundAt).getMp();
            ec = provincialCumulativeConfirms.get(foundAt).getEc();
            fs = provincialCumulativeConfirms.get(foundAt).getFs();
            unknown = provincialCumulativeConfirms.get(foundAt).getUnknown();
        } else if (foundAt > 0){
            ProvincialCumulativeConfirmedModel model = provincialCumulativeConfirms.get(foundAt);
            ProvincialCumulativeConfirmedModel model2 = provincialCumulativeConfirms.get(foundAt - 1);

            if (isDigit(model.getGp()) && isDigit(model2.getGp()))
                gp = String.valueOf(Integer.parseInt(model.getGp()) - Integer.parseInt(model2.getGp()));

            if (isDigit(model.getWc()) && isDigit(model2.getWc()))
                wc = String.valueOf(Integer.parseInt(model.getWc()) - Integer.parseInt(model2.getWc()));

            if (isDigit(model.getKzn()) && isDigit(model2.getKzn()))
                kzn = String.valueOf(Integer.parseInt(model.getKzn()) - Integer.parseInt(model2.getKzn()));

            if (isDigit(model.getLp()) && isDigit(model2.getLp()))
                lp = String.valueOf(Integer.parseInt(model.getLp()) - Integer.parseInt(model2.getLp()));

            if (isDigit(model.getNw()) && isDigit(model2.getNw()))
                nw = String.valueOf(Integer.parseInt(model.getNw()) - Integer.parseInt(model2.getNw()));

            if (isDigit(model.getNc()) && isDigit(model2.getNc()))
                nc = String.valueOf(Integer.parseInt(model.getNc()) - Integer.parseInt(model2.getNc()));

            if (isDigit(model.getFs()) && isDigit(model2.getFs()))
                fs = String.valueOf(Integer.parseInt(model.getFs()) - Integer.parseInt(model2.getFs()));

            if (isDigit(model.getEc()) && isDigit(model2.getEc()))
                ec = String.valueOf(Integer.parseInt(model.getEc()) - Integer.parseInt(model2.getEc()));

            if (isDigit(model.getMp()) && isDigit(model2.getMp()))
                mp = String.valueOf(Integer.parseInt(model.getMp()) - Integer.parseInt(model2.getMp()));

            if (isDigit(model.getUnknown()) && isDigit(model2.getUnknown()))
                unknown = String.valueOf(Integer.parseInt(model.getUnknown()) - Integer.parseInt(model2.getUnknown()));
        }

        map.put(Constants.GAUTENG, gp);
        map.put(Constants.WESTERN_CAPE, wc);
        map.put(Constants.KWA_ZULU_NATAL, kzn);
        map.put(Constants.LIMPOPO, lp);
        map.put(Constants.NORTH_WEST, nw);
        map.put(Constants.NORTHERN_CAPE, nc);
        map.put(Constants.FREE_STATE, fs);
        map.put(Constants.EASTERN_CAPE, ec);
        map.put(Constants.MPUMALANGA, mp);
        map.put(Constants.UNKNOWN, unknown);
        map.put(Constants.DATE, date);

        return map;
    }

    /**
     * Gets total positive cases for each province
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getProvincialPositives(){
        ProvincialCumulativeConfirmedModel model = null;
        HashMap<String, String> map = new HashMap<String, String>();
        int x = provincialCumulativeConfirms.size() - 1;
        String gp, wc, kzn, lp, nw, nc, fs, ec, mp, unknown;

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
                unknown = model.getUnknown().equals("") ? "0" : model.getUnknown();

                x--;
                if (x < 0) break;
            } while (allZero(gp, wc, mp, ec, nc, nw, lp, kzn, fs));
        }catch (Exception e){
            gp = wc = kzn = lp = nw = nc = ec = fs = mp = unknown = "0";
        }
        String date;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").format(model.getDate());
        } catch (Exception e){
            date = "";
        }

        map.put(Constants.GAUTENG, gp);
        map.put(Constants.WESTERN_CAPE, wc);
        map.put(Constants.KWA_ZULU_NATAL, kzn);
        map.put(Constants.LIMPOPO, lp);
        map.put(Constants.NORTH_WEST, nw);
        map.put(Constants.NORTHERN_CAPE, nc);
        map.put(Constants.FREE_STATE, fs);
        map.put(Constants.EASTERN_CAPE, ec);
        map.put(Constants.MPUMALANGA, mp);
        map.put(Constants.UNKNOWN, unknown);
        map.put(Constants.DATE, date);

        return map;
    }

    /**
     * Gets total positive cases for the entire nation
     * @return String
     */
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

    /**
     * Gets total recoveries for the entire nation
     * @return String
     */
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

    /**
     * Gets total deaths for the entire nation
     * @return String
     */
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

    /**
     * Gets total tests completed for the entire nation
     * @return String
     */
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

    public NewsModel getNewsModel() {
        return newsModel;
    }

    /**
     *
     * @param newsModel
     */
    public void setNewsModel(@NonNull NewsModel newsModel) {
        this.newsModel = newsModel;
    }

    /**
     * Checks whether the String has a numeric value, then returns true. Returns false otherwise
     * @param s String to be checked
     * @return boolean
     */
    private boolean isDigit(String s){
        boolean isValid = true;

        if (s.equals("") ) isValid = false;

        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c))
                isValid = false;
        }

        return isValid;
    }

    /**
     * Checks whether each and every String has a value of 0, then returns true. Returns false otherwise.
     * @param s list of Strings to be checked
     * @return boolean
     */
    private boolean allZero(String ...s){
        boolean allZero = true;
        for (String each : s){
            if (!each.equals("0"))
                allZero = false;
        }

        return allZero;
    }
}
