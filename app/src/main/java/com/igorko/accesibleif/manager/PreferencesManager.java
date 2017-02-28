package com.igorko.accesibleif.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.igorko.accesibleif.BuildConfig;
import com.igorko.accesibleif.app.AppContext;
import com.igorko.accesibleif.utils.Const;

/**
 * Created by Igorko on 09.11.2016.
 */

public class PreferencesManager implements Const{

    private static PreferencesManager mInstance;
    private static SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance());
    private static SharedPreferences.Editor mEditor = mSharedPref.edit();

    public static PreferencesManager getInstance() {
        if (mInstance == null) {
            mInstance = new PreferencesManager();
        }
        return mInstance;
    }

    //Check if App Start First Time
    public int appGetFirstTimeRun() {
        SharedPreferences appPreferences = AppContext.getInstance().getSharedPreferences(APPLICATION, 0);
        int appCurrentBuildVersion = BuildConfig.VERSION_CODE;
        int appLastBuildVersion = appPreferences.getInt(APP_FIRST_START_PREFERENCE, 0);

        if (appLastBuildVersion == appCurrentBuildVersion ) {
            return APP_STARTED_EVER;
        } else {
            if (appLastBuildVersion == 0) {
                return APP_STARTED_FIRST_TIME;
            } else {
                return APP_STARTED_FIST_TIME_AFTER_UPDATE;
            }
        }
    }

    public void setAppNotFirstTime(){
        SharedPreferences appPreferences = AppContext.getInstance().getSharedPreferences(APPLICATION, 0);
        int appCurrentBuildVersion = BuildConfig.VERSION_CODE;
        appPreferences.edit().putInt(APP_FIRST_START_PREFERENCE, appCurrentBuildVersion).apply();
    }

    public static void setMapLimitSettings(boolean isMapLimit){
        mEditor.putBoolean(MAP_LIMIT_PREFERENCE, isMapLimit).commit();
    }

    public static boolean isMapLimitSetted(){
        return mSharedPref.getBoolean(MAP_LIMIT_PREFERENCE, false);
    }

    public static void setLocationFollowingSettings(boolean isFollowing){
        mEditor.putBoolean(FOLLOW_PREFERENCE, isFollowing).commit();
    }

    public static boolean isFollowingLocation(){
        return mSharedPref.getBoolean(FOLLOW_PREFERENCE, false);
    }
}
