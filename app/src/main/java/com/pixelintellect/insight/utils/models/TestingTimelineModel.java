package com.pixelintellect.insight.utils.models;

import java.util.Date;

public class TestingTimelineModel {
    private Date date;
    private String cumulativeTests;
    private String cumulativeRecovered;
    private String cumulativeDeaths;

    public TestingTimelineModel(Date date, String cumulativeTests, String cumulativeRecovered, String cumulativeDeaths) {
        this.date = date;
        this.cumulativeTests = cumulativeTests;
        this.cumulativeRecovered = cumulativeRecovered;
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

    public String getCumulativeRecovered() {
        return cumulativeRecovered;
    }

    public void setCumulativeRecovered(String cumulativeRecovered) {
        this.cumulativeRecovered = cumulativeRecovered;
    }

    public String getCumulativeDeaths() {
        return cumulativeDeaths;
    }

    public void setCumulativeDeaths(String cumulativeDeaths) {
        this.cumulativeDeaths = cumulativeDeaths;
    }
}
