package com.igorko.accesibleif.models;

/**
 * Created by Igorko on 13.02.2017.
 */
public class City {

    private String cityName;
    private float cityCenterLatitude;
    private float cityCenterLontitude;
    private float cityRadius;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public float getCityCenterLatitude() {
        return cityCenterLatitude;
    }

    public void setCityCenterLatitude(float cityCenterLatitude) {
        this.cityCenterLatitude = cityCenterLatitude;
    }

    public float getCityCenterLontitude() {
        return cityCenterLontitude;
    }

    public void setCityCenterLontitude(float cityCenterLontitude) {
        this.cityCenterLontitude = cityCenterLontitude;
    }

    public float getCityRadius() {
        return cityRadius;
    }

    public void setCityRadius(float cityRadius) {
        this.cityRadius = cityRadius;
    }
}
