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
import com.igorko.accesibleif.app.AppContext;
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
    private MarkerOptions mCurrentLocationMarker;
    private Marker mLastCurrentLocationMarker;

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
                for (Element element : elementList) {
                    if(element != null){
                        BitmapDescriptor markerIcon = null;
                        if (element.getTags() != null && element.getTags().getAmenity() != null) {
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
                            Tags tags = element.getTags();
                            if (tags != null && tags.getShop() != null) {
                                markerIcon = IconsUtils.setShopIcon(element);
                            } else if (element.getTags() != null) {
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
                                } else {
                                    markerIcon = IconsUtils.setDefaultIcon(element);
                                }
                            } else {
                                markerIcon = IconsUtils.setDefaultIcon(element);
                            }
                        }
                        MarkerOptions marker = addMarkerElement(element, markerIcon);
                        markerList.add(marker);
                    }
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

    private MarkerOptions addMarkerElement(Element element, BitmapDescriptor markerIcon) {

        MarkerOptions mapMarker = null;

        if(element != null && element.getTags() != null && markerIcon != null){
            String operator = element.getTags().getOperator();
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

            String markerName;
            if(operator!= null && !operator.isEmpty()){
                markerName = operator;
            }else {
                markerName = name;
            }

            if (markerName == null || markerName.isEmpty()) {
                markerName = AppContext.getInstance().getString(R.string.no_name);
            }

            mapMarker = new MarkerOptions().position(coordinates).title(markerName);
            mapMarker.icon(markerIcon);
        }

        return mapMarker;
    }

    public ArrayList<MarkerOptions> addMarkers(GoogleMap googleMap, ArrayList<MarkerOptions> markerList) {
        if (googleMap != null) {
            googleMap.clear();

            if(mCurrentLocationMarker != null) {
                mLastCurrentLocationMarker = googleMap.addMarker(mCurrentLocationMarker);
            }

            if (markerList != null && !markerList.isEmpty()) {
                for (MarkerOptions markerOptions : markerList) {
                    if (markerOptions != null) {
                        googleMap.addMarker(markerOptions);
                    }
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
            
            mCurrentLocationMarker = new MarkerOptions()
                    .title(pinName)
                    .position(currentPosition)
                    .icon(bitmapDescriptor);

            if (map != null && mCurrentLocationMarker != null) {
                if(mLastCurrentLocationMarker != null){
                    mLastCurrentLocationMarker.remove();
                }
                mLastCurrentLocationMarker = map.addMarker(mCurrentLocationMarker);

                if (PreferencesManager.isFollowingLocation()) {
                    map.animateCamera(CameraUpdateFactory.newLatLng(currentPosition));
                }
            }
        }
        return mLastCurrentLocationMarker;
    }
}
