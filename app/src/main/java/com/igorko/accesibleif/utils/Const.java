package com.igorko.accesibleif.utils;

/**
 * Created by Igorko on 10.10.2016.
 */

public interface Const {

    enum BuildingsType{
        ALL, HOSPITALS, PHARMACIES, SHOPS, ATMs;
    }

    String TAG = "tag";
    String BASE_URL = "http://overpass-api.de";

    int ALL_ACSSESIBLE_BUILDINGS = 1;
    int HOSPITAL_ACSSESIBLE_BUILDINGS = 2;
    int PHARMACY_ACSSESIBLE_BUILDINGS = 3;
    int SHOP_BUILDINGS = 4;
    int ATM_BUILDINGS = 5;

    int SETTINGS = 7;
    int HOW_ITS_WORK = 8;
    int ABOUT_US = 9;

    String AMENITY_HOSPITAL = "hospital";
    String AMENITY_PHAPMACY = "pharmacy";
    String AMENITY_SHOP = "shop";
    String AMENITY_ATM = "atm";

    String AERODROME = "aerodrome";
    String STATION = "station";
    String GOVERNMENT = "government";

    String WHEELCHAIR_YES = "yes";
    String WHEELCHAIR_NO = "no";
    String WHEELCHAIR_LIMIITED = "limited";

    String ABOUT_FRAGMENT_TAG = "about";
    String SETTINGS_FRAGMENT_TAG = "settings";
    String HOW_ITS_WORK_TAG = "how_its_work";

    String LOCATION_DATA_FILTER_NAME = "GPSLocationUpdates";
    int REQUEST_LOCATION = 10;

    String FOLLOW_PREFERENCE = "follow_locaton";
    String MAP_LIMIT_PREFERENCE = "map_limit";

    float TINY_ZOOM_LEVEL = 16.0f;
    float MAX_ZOOM_LEVEL = 12.3f;

    double MIN_LAT_IF_AREA = 48.87;
    double MAX_LAT_IF_AREA = 48.94;
    double MIN_LON_IF_AREA = 24.75;
    double MAX_LON_IF_AREA = 24.69;

//    TODO
//    String CITY_CENTER_LATITUDE_PREFERENCE = "city_center_latitude";
//    String CITY_CENTER_LONGITUDE_PREFERENCE = "city_center_longitude";
//    String CITY_RADIUS = "city_radius";

    int RECENT_BACK_PRESSED_TIME = 2000;
}
