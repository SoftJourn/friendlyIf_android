package com.igorko.accesibleif.utils;

import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.igorko.accesibleif.R;

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
        double cameraLontitude = cameraTarget.longitude;

        double ratushaLatitude = (double)(NumberUtils.getFloat(R.dimen.ratusha_latitude));
        double ratushaLontitute = (double)(NumberUtils.getFloat(R.dimen.ratusha_longitude));

        double deltaLat = cameraLatitude - ratushaLatitude;
        double deltaLon = cameraLontitude - ratushaLontitute;

        float cityRadius = NumberUtils.getFloat(R.dimen.if_city_radius);
        float circleIfDistance = cityRadius / NumberUtils.getFloat(R.dimen.earth_diameter) * 360.0f;
        float circlePointDistance = (float) Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLon, 2));

        double distanceKoef = circleIfDistance / circlePointDistance;

        if (distanceKoef < 1) {
            double pointLat = ratushaLatitude + deltaLat * distanceKoef;
            double pointLon = ratushaLontitute + deltaLon * distanceKoef;

            return new LatLng(pointLat, pointLon);
        } return null;
    }

    public static void moveToCenterIf(GoogleMap googleMap, boolean zoom, boolean mapIsTouched) {
        float ratushaLatitude = NumberUtils.getFloat(R.dimen.ratusha_latitude);
        float ratushaLongitude = NumberUtils.getFloat(R.dimen.ratusha_longitude);

        LatLng centerIFPosition = new LatLng(ratushaLatitude, ratushaLongitude);
        if (googleMap != null) {
            Log.d(TAG, Boolean.valueOf(mapIsTouched).toString());
            if (!mapIsTouched) {
                if (zoom) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerIFPosition, 18.0f));
                } else {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(centerIFPosition));
                }
            }
        }
    }
}
