package com.igorko.accesibleif.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.igorko.accesibleif.app.AppContext;
import com.igorko.accesibleif.utils.Const;

/**
 * Created by Igorko on 09.11.2016.
 */

public class PreferencesManager implements Const{

    private static PreferencesManager mInstance;
    private static SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance());
    private static SharedPreferences.Editor mEditor = mSharedPref.edit();
    private Boolean mAppFirstStart = null;

    public static PreferencesManager getInstance() {
        if (mInstance == null) {
            mInstance = new PreferencesManager();
        }
        return mInstance;
    }

    public boolean isFirstTime() {
        if (mAppFirstStart == null) {
            SharedPreferences preferences = AppContext.getInstance().getSharedPreferences(APP_FIRST_START_PREFERENCE, Context.MODE_PRIVATE);
            mAppFirstStart = preferences.getBoolean(APP_FIRST_START_PREFERENCE, true);
        }
        return mAppFirstStart;
    }

    public void setAppFirstStart(){
        SharedPreferences preferences = AppContext.getInstance().getSharedPreferences(APP_FIRST_START_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_FIRST_START_PREFERENCE, false);
        editor.commit();
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
