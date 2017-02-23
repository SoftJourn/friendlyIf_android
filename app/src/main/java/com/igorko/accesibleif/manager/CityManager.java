package com.igorko.accesibleif.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.igorko.accesibleif.app.AppContext;
import com.igorko.accesibleif.models.City;
import com.igorko.accesibleif.utils.Const;
import java.util.List;

/**
 * Created by Igorko on 13.02.2017.
 */

public class CityManager implements Const {

    private static CityManager mInstance;
    private static final int DEFAULT_CITY_ID = 0;
    static SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance());
    static SharedPreferences.Editor mEditor = mSharedPref.edit();
    private CityStorage mCityStorage;
    private boolean cityWasRecentlyChanged = false;

    public static CityManager getInstance() {
        if (mInstance == null) {
            mInstance = new CityManager();
        }
        return mInstance;
    }

    public CityManager() {
        mCityStorage = CityStorage.getInstance();
    }

    public boolean isCityWasRecentlyChanged() {
        return cityWasRecentlyChanged;
    }

    public void setCityWasRecentlyChanged(boolean cityWasRecentlyChanged) {
        this.cityWasRecentlyChanged = cityWasRecentlyChanged;
    }

    public void setCurrentCity(City currentCity) {
        mEditor.putInt(CURRENT_CITY_ID_PREFERENCE, currentCity.getCityId());
        mEditor.commit();
    }

    public City getCurrentCity() {
        int cityId = mSharedPref.getInt(CURRENT_CITY_ID_PREFERENCE, DEFAULT_CITY_ID);
        return mCityStorage.getCityById(cityId);
    }

    public List<City> getCitiesList(){
        return mCityStorage.getCitiesList();
    }

    public String[] getCitiesNames(){
        int length = getCitiesList().size();
        String[] names = new String[length];
        for(int i=0; i<length; i++){
            names[i]=getCitiesList().get(i).getCityName();
        }
        return names;
    }

    public String[] getCitiesId(){
        int length = getCitiesList().size();
        String[] ids = new String[length];
        for(int i=0; i<length; i++){
            ids[i]=String.valueOf(getCitiesList().get(i).getCityId());
        }
        return ids;
    }

    public City getCityById(int id){
        return mCityStorage.getCityById(id);
    }
}
