package com.example.ex192;

import android.animation.ValueAnimator;

/**
 * The type vaccine info class
 * @author Itey Weintraub <av5350@bs.amalnet.k12.il>
 * @version	1
 * @since 28.3.2021
 * short description:
 *
 *      This class represent a vaccine (with place and date)
 */
public class VaccineInfo {
    private String place;
    private String date;

    /**
     * Instantiates a new Vaccine info.
     */
    public VaccineInfo(){}

    public VaccineInfo(String place, String date)
    {
        this.place = place;
        this.date = date;
    }

    /**
     * Gets place.
     *
     * @return the place
     */
    public String getPlace()
    {
        return place;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate()
    {
        return date;
    }
}
