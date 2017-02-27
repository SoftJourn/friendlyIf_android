package com.igorko.accesibleif.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Igorko on 13.02.2017.
 */
public class City {

    private int cityId;
    private String cityName;
    private double cityRadius;
    private LatLng cityCenter;
    private LatLng topRectPoint;
    private LatLng bottomRectPoint;

    public City(int cityId, String cityName, float cityRadius, LatLng cityCenter) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityCenter = cityCenter;
        this.cityRadius = cityRadius;
    }

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

    public LatLng getCityCenter() {
        return cityCenter;
    }

    public void setCityCenter(LatLng cityCenter) {
        this.cityCenter = cityCenter;
    }

    public LatLng getTopRectPoint() {
        double lat = cityCenter.latitude;
        double lon = cityCenter.longitude;

        double kmInLongitudeDegree = 111.320 * Math.cos( lat / 180.0 * Math.PI);
        double radiusInKm = getCityRadius();

        double deltaLat = radiusInKm / 111.1;
        double deltaLong = radiusInKm / kmInLongitudeDegree;

        double maxLat = lat + deltaLat;
        double maxLon = lon + deltaLong;

        return new LatLng(maxLat, maxLon);
    }

    public LatLng getBottomRectPoint() {
        double lat = cityCenter.latitude;
        double lon = cityCenter.longitude;

        double kmInLongitudeDegree = 111.320 * Math.cos( lat / 180.0 * Math.PI);
        double radiusInKm = getCityRadius();

        double deltaLat = radiusInKm / 111.1;
        double deltaLong = radiusInKm / kmInLongitudeDegree;

        double minLat = lat - deltaLat;
        double minLon = lon - deltaLong;

        return new LatLng(minLat, minLon);
    }
}
