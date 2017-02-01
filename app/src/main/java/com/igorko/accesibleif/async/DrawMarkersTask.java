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
import com.igorko.accesibleif.utils.Const;
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
        ArrayList<MarkerOptions> markerList = MarkerUtils.getInstance().getAllMarkers(mElementList, mZoomLevel);
        return markerList;
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