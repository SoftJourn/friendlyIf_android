package com.igorko.accesibleif.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.igorko.accesibleif.activity.MainActivity;
import com.igorko.accesibleif.models.Bounds;
import com.igorko.accesibleif.models.Element;
import com.igorko.accesibleif.models.Tags;
import com.igorko.accesibleif.utils.Const;
import com.igorko.accesibleif.utils.IconsUtils;
import com.igorko.accesibleif.utils.MarkerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igorko on 01.11.2016.
 */

public class DrawMarkersTask extends AsyncTask<Float, Object, ArrayList<MarkerOptions>> implements Const {

    private Activity mActivity;
    private float mZoomLevel;
    private List<Element> mElementList;
    private ArrayList<MarkerOptions> mMarkerList;

    public DrawMarkersTask(MainActivity activity, List<Element> elementList,
                           ArrayList<MarkerOptions> markerList) {
        mActivity = activity;
        mElementList = elementList;
        mMarkerList = new ArrayList<>(markerList);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<MarkerOptions> doInBackground(Float... params) {
            Log.d("Zoom", "Zoom: " + params[0]);
        mZoomLevel = params[0];

        if (mElementList != null && mElementList.size() > 0) {
            if ((mZoomLevel == 0.0) || (mZoomLevel >= TINY_ZOOM_LEVEL)) {
                mMarkerList.clear();
                BitmapDescriptor markerIcon = null;
                for (Element element : mElementList) {
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
                    mMarkerList.add(marker);
                }
            } else if (mZoomLevel < TINY_ZOOM_LEVEL) {
                mMarkerList.clear();
                for (Element element : mElementList) {
                    if (element != null) {
                        BitmapDescriptor markerIcon = IconsUtils.setTinyIcon(element);
                        MarkerOptions marker = addMarkerElement(element, markerIcon);
                        mMarkerList.add(marker);
                    }
                }
            }
        }
        return mMarkerList;
    }

    @Override
    protected void onPostExecute(ArrayList<MarkerOptions> markerList) {
        super.onPostExecute(markerList);
        if (markerList != null) {
            MainActivity activity = ((MainActivity) mActivity);
            activity.initMarkerList(markerList);
            
            MarkerUtils.getInstance().addMarkers
                    (activity.getMap(), markerList);
        }
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

        MarkerOptions mapMarker = new MarkerOptions().position(coordinates).title(name);
        mapMarker.icon(markerIcon);
        return mapMarker;
    }
}