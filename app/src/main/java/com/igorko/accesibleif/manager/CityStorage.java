package com.igorko.accesibleif.manager;

import com.google.android.gms.maps.model.LatLng;
import com.igorko.accesibleif.models.City;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Igorko on 22.02.2017.
 */

public class CityStorage {

    public List<City> mCitiesList;

    public CityStorage() {
        initCitiesList();
    }

    private void initCitiesList(){
        City ifCity = new City(0, "Івано-Франківськ", new LatLng(48.922712, 24.710207), 10.0d);
        City lvivCity = new City(1, "Львів", new LatLng(49.841019, 24.028023), 15.0d);

        mCitiesList = new LinkedList<>();
        mCitiesList.add(ifCity);
        mCitiesList.add(lvivCity);

//        TODO
//        City chernivciCity = new City(2, "Чернівці", new LatLng(48.2903, 25.9354), 8.0d);
//        mCitiesList.add(chernCity);
    }

    public List<City> getCitiesList(){
        return mCitiesList;
    }

    public City getCityById(int id){
        for (City city:mCitiesList){
            if (city.getCityId() == id){
                return city;
            }
        }
        return null;
    }
}
