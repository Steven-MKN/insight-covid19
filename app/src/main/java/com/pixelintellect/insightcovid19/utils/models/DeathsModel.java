package com.pixelintellect.insightcovid19.utils.models;

import java.util.Date;

public class DeathsModel {
    public DeathsModel(Date date, String province){
        this.date = date;
        this.province = province;
    }
    private Date date;
    private String province;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}



