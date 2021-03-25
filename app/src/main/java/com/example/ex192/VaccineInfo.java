package com.example.ex192;

import android.animation.ValueAnimator;

public class VaccineInfo {
    private String place;
    private String date;

    public VaccineInfo(){}

    public VaccineInfo(String place, String date)
    {
        this.place = place;
        this.date = date;
    }

    public String getPlace()
    {
        return place;
    }

    public String getDate()
    {
        return date;
    }
}
