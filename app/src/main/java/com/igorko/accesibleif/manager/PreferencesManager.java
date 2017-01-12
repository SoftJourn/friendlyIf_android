package com.igorko.accesibleif.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.igorko.accesibleif.app.AppContext;
import com.igorko.accesibleif.utils.Const;

/**
 * Created by Igorko on 09.11.2016.
 */

public class PreferencesManager implements Const{

    static SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance());
    static SharedPreferences.Editor mEditor = mSharedPref.edit();

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
