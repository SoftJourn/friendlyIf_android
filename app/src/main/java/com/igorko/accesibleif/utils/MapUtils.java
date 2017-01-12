package com.igorko.accesibleif.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.igorko.accesibleif.services.LocationService;

/**
 * Created by Igorko on 26.10.2016.
 */

public class MapUtils implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Const {

    private static MapUtils mInstance;
    private GoogleApiClient mGoogleApiClient;
    private Activity mActivity;
    private Intent mLocationServiceIntent;

    public static MapUtils getInstance() {
        if (mInstance == null) {
            mInstance = new MapUtils();
        }
        return mInstance;
    }

    public void initGoogleApiClient(Activity activity) {
        mActivity = activity;

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void onStart() {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "onConnected");

        if(mActivity != null) {
            mLocationServiceIntent = new Intent(mActivity, LocationService.class);
            mActivity.startService(mLocationServiceIntent);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        if(mActivity != null && mLocationServiceIntent != null) {
            mActivity.stopService(mLocationServiceIntent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        if(mActivity != null && mLocationServiceIntent != null) {
            mActivity.stopService(mLocationServiceIntent);
        }
    }

    public void onStop() {
        if(mActivity != null && mLocationServiceIntent != null) {
            mActivity.stopService(mLocationServiceIntent);
        }
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}
