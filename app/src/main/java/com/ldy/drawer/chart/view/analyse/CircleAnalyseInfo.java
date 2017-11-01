package com.ldy.drawer.chart.view.analyse;

/**
 * Created by lidongyang on 2017/10/20.
 */
public class CircleAnalyseInfo {

    private String value;
    private float rate;

    public CircleAnalyseInfo() {
    }

    public CircleAnalyseInfo(String value, float rate) {
        this.value = value;
        this.rate = rate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

}
