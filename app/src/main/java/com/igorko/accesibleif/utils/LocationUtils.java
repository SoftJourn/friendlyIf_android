package com.igorko.accesibleif.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import com.igorko.accesibleif.app.AppContext;

/**
 * Created by Igorko on 25.10.2016.
 */

public class LocationUtils implements Const {

    private static Context mContext = AppContext.getInstance();
    private static LocationUtils mInstance;

    public static LocationUtils getInstance() {
        if (mInstance == null) {
            mInstance = new LocationUtils();
        }
        return mInstance;
    }

    public static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}