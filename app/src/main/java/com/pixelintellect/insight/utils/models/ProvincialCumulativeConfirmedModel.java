package com.pixelintellect.insight.utils.models;

import java.util.Date;

public class ProvincialCumulativeConfirmedModel {
    private Date date;
    private String ec, gp, wc, nc, fs, lp, kzn, nw, mp, unknown,total;

    public ProvincialCumulativeConfirmedModel(Date date, String ec, String gp, String wc, String nc, String fs, String lp, String kzn, String nw, String mp, String unknown, String total) {
        this.date = date;
        this.ec = ec;
        this.gp = gp;
        this.wc = wc;
        this.nc = nc;
        this.fs = fs;
        this.lp = lp;
        this.kzn = kzn;
        this.nw = nw;
        this.mp = mp;
        this.unknown = unknown;
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getWc() {
        return wc;
    }

    public void setWc(String wc) {
        this.wc = wc;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getLp() {
        return lp;
    }

    public void setLp(String lp) {
        this.lp = lp;
    }

    public String getKzn() {
        return kzn;
    }

    public void setKzn(String kzn) {
        this.kzn = kzn;
    }

    public String getNw() {
        return nw;
    }

    public void setNw(String nw) {
        this.nw = nw;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getUnknown() {
        return unknown;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
