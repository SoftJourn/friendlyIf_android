package com.igorko.accesibleif.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.services.LocationService;

/**
 * Created by Igorko on 24.10.2016.
 */

public class Navigaton implements Extras{

    public static void goToLocationSettings(Context context){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public static void goToOpenstreetmapSite(Activity activity){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.http_open_street_map_url)));
        activity.startActivity(intent);
    }

    public static void sendEmailToDeveloper(Activity activity) {
        sendEmail(activity, activity.getString(R.string.developer_email));
    }

    public static void sendEmailToDataFiller(Activity activity) {
        sendEmail(activity, activity.getString(R.string.data_adding_email));
    }

    private static void sendEmail(Activity activity, String receiverEmail){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_info));
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.setData(Uri.parse("mailto:" + receiverEmail));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void sendStartLocationToActivity(LocationService locationService, Location location) {
        Intent intent = new Intent("GPSStartLocationUpdates");
        intent.putExtra(STATUS_EXTRA, "LocationReceivingStarted");
        intent.putExtra(LOCATION_START_EXTRA, location);
        LocalBroadcastManager.getInstance(locationService).sendBroadcast(intent);
    }

    public static void sendLocationToActivity(LocationService locationService, Location location) {
        Intent intent = new Intent("GPSLocationUpdates");
        intent.putExtra(STATUS_EXTRA, "LocationUpdated");
        intent.putExtra(LOCATION_EXTRA, location);
        LocalBroadcastManager.getInstance(locationService).sendBroadcast(intent);
    }
}
