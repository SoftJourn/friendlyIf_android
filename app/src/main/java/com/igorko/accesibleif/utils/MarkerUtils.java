package com.igorko.accesibleif.utils;

import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.igorko.accesibleif.models.Tags;
import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<MarkerOptions> getAllMarkers(List<Element> elementList, float zoomLevel){
        ArrayList<MarkerOptions> markerList = new ArrayList<MarkerOptions>();
        if (elementList != null && elementList.size() > 0) {
            if ((zoomLevel == 0.0) || (zoomLevel >= TINY_ZOOM_LEVEL)) {
                BitmapDescriptor markerIcon = null;
                for (Element element : elementList) {
                    if (element != null && element.getTags() != null && element.getTags().getAmenity() != null) {
                        String amenity = element.getTags().getAmenity();
                        if (amenity != null) {
                            switch (amenity) {
                                case AMENITY_PHAPMACY: {
                                    markerIcon = IconsUtils.setPharmacyIcon(element);
                                    break;
                                }
                                case AMENITY_SHOP: {
                                    markerIcon = IconsUtils.setShopIcon(element);
                                    break;
                                }
                                case AMENITY_HOSPITAL: {
                                    markerIcon = IconsUtils.setHospitalIcon(element);
                                    break;
                                }
                                case AMENITY_ATM: {
                                    markerIcon = IconsUtils.setATMIcon(element);
                                    break;
                                }
                                default: {
                                    markerIcon = IconsUtils.setDefaultIcon(element);
                                    break;
                                }
                            }
                        }
                    } else {
                        if(element != null) {
                            Tags tags = element.getTags();
                            if (tags != null && tags.getShop() != null) {
                                markerIcon = IconsUtils.setShopIcon(element);
                            }
                            if (element.getTags() != null) {
                                if (element.getTags().getRailway() != null) {
                                    String railway = element.getTags().getRailway();
                                    if (railway.equals(STATION)) {
                                        markerIcon = IconsUtils.setRailwayIcon(element);
                                    }
                                } else if (element.getTags().getAeroway() != null) {
                                    String aeroway = element.getTags().getAeroway();
                                    if (aeroway.equals(AERODROME)) {
                                        markerIcon = IconsUtils.setAirportIcon(element);
                                    }
                                } else if (element.getTags().getOffice() != null) {
                                    String office = element.getTags().getOffice();
                                    if (office != null && office.equals(GOVERNMENT)) {
                                        markerIcon = IconsUtils.setAdministrationIcon(element);
                                    }
                                }
                            }
                        }
                    }
                    MarkerOptions marker = addMarkerElement(element, markerIcon);
                    markerList.add(marker);
                }
            } else if (zoomLevel < TINY_ZOOM_LEVEL) {
                markerList.clear();
                for (Element element : elementList) {
                    if (element != null) {
                        BitmapDescriptor markerIcon = IconsUtils.setTinyIcon(element);
                        MarkerOptions marker = addMarkerElement(element, markerIcon);
                        markerList.add(marker);
                    }
                }
            }
        }
        return markerList;
    }

    private ArrayList<MarkerOptions> fillMarkerList(ArrayList<MarkerOptions> markerList, Element element, BitmapDescriptor markerIcon){
        if (element != null && markerIcon != null) {
            MarkerOptions marker = addMarkerElement(element, markerIcon);
            markerList.add(marker);
        }
        return markerList;
    }

    public ArrayList<MarkerOptions> getPharmacyMarkers(ArrayList<Element> elementList){
        ArrayList<MarkerOptions> markerList = new ArrayList();
        for (Element element : elementList) {
            if (element != null) {
                markerList = fillMarkerList(markerList, element, IconsUtils.setPharmacyIcon(element));
            }
        }
        return markerList;
    }

    public ArrayList<MarkerOptions> getHospitalMarkers(ArrayList<Element> elementList){
        ArrayList<MarkerOptions> markerList = new ArrayList();
        for (Element element : elementList) {
            if (element != null) {
                markerList = fillMarkerList(markerList, element, IconsUtils.setHospitalIcon(element));
            }
        }
        return markerList;
    }

    public ArrayList<MarkerOptions> getShopMarkers(ArrayList<Element> elementList){
        ArrayList<MarkerOptions> markerList = new ArrayList();
        for (Element element : elementList) {
            if (element != null) {
                markerList = fillMarkerList(markerList, element, IconsUtils.setShopIcon(element));
            }
        }
        return markerList;
    }

    public ArrayList<MarkerOptions> getATMMarkers(ArrayList<Element> elementList){
        ArrayList<MarkerOptions> markerList = new ArrayList();
        for (Element element : elementList) {
            if (element != null) {
                markerList = fillMarkerList(markerList, element, IconsUtils.setATMIcon(element));
            }
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

    public void initZoomLevel(float previousZoomLevel){
        this.mPreviousZoomLevel = previousZoomLevel;
    }

    public float getPreveuosZoomLevel(){
        return mPreviousZoomLevel;
    }

    public void drawMarkers(MainActivity activity, ArrayList<MarkerOptions> markersList, float currentZoom,
                             ArrayList<Element> elementList, float previousZoomLevel) {
        if (markersList != null && !markersList.isEmpty()) {
            if ((((previousZoomLevel != currentZoom)) && (((previousZoomLevel < TINY_ZOOM_LEVEL) &&
                    (currentZoom >= TINY_ZOOM_LEVEL)) || ((previousZoomLevel >= TINY_ZOOM_LEVEL) && (currentZoom < TINY_ZOOM_LEVEL))))) {
                DrawMarkersTask drawMarkersTask = new DrawMarkersTask(activity, elementList, markersList);
                drawMarkersTask.execute(currentZoom);
                mPreviousZoomLevel = currentZoom;
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
