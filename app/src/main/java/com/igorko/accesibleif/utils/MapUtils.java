package com.igorko.accesibleif.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Igorko on 01.02.2017.
 */

public class MapUtils implements Const{

    public static void setCheckLimits(GoogleMap map, CameraPosition cameraPosition, boolean mapIsTouched, float currentZoom){
        if (cameraPosition != null && !mapIsTouched) {
            LatLng newCameraPosition = cameraPosition.target;
            if ((((newCameraPosition.latitude < MIN_LAT_IF_AREA) || newCameraPosition.latitude > MAX_LAT_IF_AREA)
                    || (newCameraPosition.longitude < MIN_LON_IF_AREA) || (newCameraPosition.longitude > MAX_LON_IF_AREA))) {
                CameraUtils.limitCameraCityArea(map, newCameraPosition, currentZoom);
            }
        }
    }
}
