package com.igorko.accesibleif.utils;

import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.manager.CityManager;
import com.igorko.accesibleif.models.City;

/**
 * Created by Igorko on 16.11.2016.
 */

public class CameraUtils implements Const {

    public static void limitCameraCityArea(GoogleMap map, LatLng cameraTarget, float currentZoom){
        LatLng newCameraTarget = moveIntoCityCircleArea(cameraTarget);
        if (newCameraTarget != null) {
            map.animateCamera(CameraUpdateFactory.newLatLng(newCameraTarget));
        }
        if (currentZoom < MAX_ZOOM_LEVEL) {
            map.animateCamera(CameraUpdateFactory.zoomTo(MAX_ZOOM_LEVEL));
        }
    }

    private static LatLng moveIntoCityCircleArea(LatLng cameraTarget){
        double cameraLatitude = cameraTarget.latitude;
        double cameraLongitude = cameraTarget.longitude;

        CityManager cityManager = new CityManager();
        City currentCity = cityManager.getCurrentCity();
        LatLng cityCenterCoordinates = currentCity.getCityCenter();

        double cityCenterLatitude = cityCenterCoordinates.latitude;
        double cityCenterLongitute = cityCenterCoordinates.longitude;

        double deltaLat = cameraLatitude - cityCenterLatitude;
        double deltaLon = cameraLongitude - cityCenterLongitute;
        float cityRadius = currentCity.getCityRadius();
        
        float circleCityDistance = cityRadius / NumberUtils.getFloat(R.dimen.earth_diameter) * 360.0f;
        float circlePointDistance = (float) Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLon, 2));

        double distanceKoef = circleCityDistance / circlePointDistance;

        if (distanceKoef < 0.9) {
            double pointLat = cityCenterLatitude + deltaLat * distanceKoef;
            double pointLon = cityCenterLongitute + deltaLon * distanceKoef;

            return new LatLng(pointLat, pointLon);
        } return null;
    }

    public static void moveToCenterCity(GoogleMap googleMap, boolean zoom, boolean mapIsTouched) {
        CityManager cityManager = new CityManager();
        City currentCity = cityManager.getCurrentCity();
        LatLng cityCenterCoordinates = currentCity.getCityCenter();

        if (googleMap != null) {
            Log.d(TAG, Boolean.valueOf(mapIsTouched).toString());
            if (!mapIsTouched) {
                if (zoom) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cityCenterCoordinates, 18.0f));
                } else {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(cityCenterCoordinates));
                }
            }
        }
    }
}
