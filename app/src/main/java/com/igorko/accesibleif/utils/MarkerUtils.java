package com.igorko.accesibleif.utils;

import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.activity.MainActivity;
import com.igorko.accesibleif.app.AppContext;
import com.igorko.accesibleif.async.DrawMarkersTask;
import com.igorko.accesibleif.manager.PreferencesManager;
import com.igorko.accesibleif.models.Bounds;
import com.igorko.accesibleif.models.Element;
import java.util.ArrayList;

/**
 * Created by Igorko on 28.11.2016.
 */

public class MarkerUtils implements Const{

    private static MarkerUtils mInstance;
    private float mPreviousZoomLevel = (float) 0.0;
    private Marker mMarker = null;

    public static MarkerUtils getInstance() {
        if (mInstance == null) {
            mInstance = new MarkerUtils();
        }
        return mInstance;
    }

    public ArrayList<MarkerOptions> getAllMarkers(ArrayList<Element> elementList){
        ArrayList<MarkerOptions> markerList = null;
        if(elementList != null && elementList.size() >  0) {
            markerList = new ArrayList();
            for (Element element : elementList) {
                if (element != null) {
                    markerList = fillMarkerList(element, IconsUtils.setDefaultIcon(element));
                }
            }
        }
        return markerList;
    }

    public ArrayList<MarkerOptions> getPharmacyMarkers(ArrayList<Element> elementList){
        ArrayList<MarkerOptions> markerList = new ArrayList();
        for (Element element : elementList) {
            if (element != null) {
                markerList = fillMarkerList(element, IconsUtils.setPharmacyIcon(element));
            }
        }
        return markerList;
    }

    public ArrayList<MarkerOptions> getHospitalMarkers(ArrayList<Element> elementList){
        ArrayList<MarkerOptions> markerList = new ArrayList();
        for (Element element : elementList) {
            if (element != null) {
                markerList = fillMarkerList(element, IconsUtils.setHospitalIcon(element));
            }
        }
        return markerList;
    }

    public ArrayList<MarkerOptions> getShopMarkers(ArrayList<Element> elementList){
        ArrayList<MarkerOptions> markerList = new ArrayList();
        for (Element element : elementList) {
            if (element != null) {
                markerList = fillMarkerList(element, IconsUtils.setShopIcon(element));
            }
        }
        return markerList;
    }

    private ArrayList<MarkerOptions> fillMarkerList(Element element, BitmapDescriptor markerIcon){
        ArrayList<MarkerOptions> markerList = new ArrayList();
        if (element != null && markerIcon != null) {
            MarkerOptions marker = addMarkerElement(element, markerIcon);
            markerList.add(marker);
        }
        return markerList;
    }

    private MarkerOptions addMarkerElement(Element element, BitmapDescriptor markerIcon) {
        String name = element.getTags().getName();
        LatLng coordinates;
        if (element.getBounds() != null) {
            Bounds bounds = element.getBounds();
            double lat = (bounds.getMinlat() + bounds.getMaxlat()) / 2;
            double lon = (bounds.getMinlon() + bounds.getMaxlon()) / 2;
            coordinates = new LatLng(lat, lon);
        } else {
            coordinates = new LatLng(element.getLat(), element.getLon());
        }

        if (name == null || name.isEmpty()) {
            name = AppContext.getInstance().getString(R.string.no_name);
        }

        MarkerOptions mapMarker = new MarkerOptions().position(coordinates).title(name);
        mapMarker.icon(markerIcon);
        return mapMarker;
    }

    public float getPreveuosZoomLevel(){
        return mPreviousZoomLevel;
    }

    public void drawMarkers(MainActivity activity, GoogleMap map, ArrayList<MarkerOptions> markersList, CameraPosition cameraPosition,
                             ArrayList<Element> elementList, float previousZoomLevel, boolean mapIsTouched, boolean cameraMoved) {
        if (markersList != null && !markersList.isEmpty()) {
            float currentZoom = 0;

            if (cameraPosition != null) {
                currentZoom = cameraPosition.zoom;
            } else {
                if (previousZoomLevel != 0.0) {
                    currentZoom = previousZoomLevel;
                } else {
                    CameraUtils.moveToCenterIf(map, true, mapIsTouched);
                }
            }

            if ((!cameraMoved || ((previousZoomLevel != cameraPosition.zoom)) && (((previousZoomLevel < TINY_ZOOM_LEVEL) &&
                    (cameraPosition.zoom >= TINY_ZOOM_LEVEL)) || ((previousZoomLevel >= TINY_ZOOM_LEVEL) && (cameraPosition.zoom < TINY_ZOOM_LEVEL))))) {
                DrawMarkersTask drawMarkersTask = new DrawMarkersTask(activity, elementList, markersList);
                drawMarkersTask.execute(currentZoom);
                mPreviousZoomLevel = currentZoom;
            }

            boolean isMapLimitedEnabled = PreferencesManager.isMapLimitSetted();
            if (!mapIsTouched && isMapLimitedEnabled && cameraMoved) {
                LatLng newCameraPosition = cameraPosition.target;
                if ((((newCameraPosition.latitude < MIN_LAT_IF_AREA) || newCameraPosition.latitude > MAX_LAT_IF_AREA)
                        || (newCameraPosition.longitude < MIN_LON_IF_AREA) || (newCameraPosition.longitude > MAX_LON_IF_AREA))) {
                    CameraUtils.limitCameraCityArea(map, newCameraPosition, currentZoom);
                }
            }
        }
    }

    public ArrayList<MarkerOptions> addMarkers(GoogleMap googleMap, ArrayList<MarkerOptions> markerList) {
        if (googleMap != null) {
            googleMap.clear();
            if (markerList != null) {
                for (MarkerOptions markerOptions : markerList) {
                    googleMap.addMarker(markerOptions);
                }
            }
        }
        return markerList;
    }

    public Marker showCurrentLocation(GoogleMap map, Location myLocation) {
        if (myLocation != null) {
            LatLng currentPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            String pinName = AppContext.getInstance().getString(R.string.current_location);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.current_location);
            MarkerOptions currentPositionMarker = new MarkerOptions()
                    .title(pinName)
                    .position(currentPosition)
                    .icon(bitmapDescriptor);

            if (map != null) {
                //for updating marker
                if (mMarker != null) {
                    mMarker.remove();
                    mMarker = map.addMarker(currentPositionMarker);
                } else {
                    mMarker = map.addMarker(currentPositionMarker);
                }

                if (PreferencesManager.isFollowingLocation()) {
                    map.animateCamera(CameraUpdateFactory.newLatLng(currentPosition));
                }
            }
        }
        return mMarker;
    }
}
