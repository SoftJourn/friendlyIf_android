package com.igorko.accesibleif.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.igorko.accesibleif.app.AppContext;
import com.igorko.accesibleif.utils.Const;

/**
 * Created by Igorko on 13.02.2017.
 */

public class CityManager implements Const{

    static SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance());
    static SharedPreferences.Editor mEditor = mSharedPref.edit();

    public static void setCurrentCity(String cityName){
        mEditor.putString(CITY_NAME_PREFERENCE, cityName).commit();
    }

    public static String getCurrentCity(){
        return mSharedPref.getString(CITY_NAME_PREFERENCE, "");
    }
}
