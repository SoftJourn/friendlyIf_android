package com.igorko.accesibleif.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Igorko on 13.02.2017.
 */
public class City {

    private int cityId;
    private String cityName;
    private LatLng cityCenter;
    private double cityRadius;
    private LatLng topRectPoint;
    private LatLng bottomRectPoint;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getCityRadius() {
        return cityRadius;
    }

    public void setCityRadius(double cityRadius) {
        this.cityRadius = cityRadius;
    }

    public LatLng getTopRectPoint() {
        double delta = cityRadius*0.7071d/40000d*360d;
        return new LatLng(cityCenter.latitude+delta, cityCenter.longitude+delta);
    }

    public LatLng getBottomRectPoint() {
        double delta = cityRadius*0.7071d/40000d*360d;
        return new LatLng(cityCenter.latitude-delta, cityCenter.longitude-delta);
    }

    public LatLng getCityCenter() {
        return cityCenter;
    }

    public void setCityCenter(LatLng cityCenter) {
        this.cityCenter = cityCenter;
    }

    public City(int cityId, String cityName, LatLng cityCenter, double cityRadius) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityCenter = cityCenter;
        this.cityRadius = cityRadius;
    }
}
