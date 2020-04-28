package com.pixelintellect.insight.utils.models;

import java.util.Date;

public class TestingTimelineModel {
    private Date date;
    private String cumulativeTests;
    private String dayRecovered;
    private String cumulativeDeaths;

    public TestingTimelineModel(Date date, String cumulativeTests, String dayRecovered, String cumulativeDeaths) {
        this.date = date;
        this.cumulativeTests = cumulativeTests;
        this.dayRecovered = dayRecovered;
        this.cumulativeDeaths = cumulativeDeaths;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCumulativeTests() {
        return cumulativeTests;
    }

    public void setCumulativeTests(String cumulativeTests) {
        this.cumulativeTests = cumulativeTests;
    }

    public String getDayRecovered() {
        return dayRecovered;
    }

    public void setDayRecovered(String dayRecovered) {
        this.dayRecovered = dayRecovered;
    }

    public String getCumulativeDeaths() {
        return cumulativeDeaths;
    }

    public void setCumulativeDeaths(String cumulativeDeaths) {
        this.cumulativeDeaths = cumulativeDeaths;
    }
}
