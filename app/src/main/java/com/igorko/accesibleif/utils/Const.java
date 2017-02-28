package com.igorko.accesibleif.utils;

/**
 * Created by Igorko on 10.10.2016.
 */

public interface Const {

    enum BuildingsType{
        ALL {
            @Override
            public String getUrlQueryType() {
                return null;
            }
        }, HOSPITALS {
            @Override
            public String getUrlQueryType() {
                return "hospital";
            }
        }, PHARMACIES {
            @Override
            public String getUrlQueryType() {
                return "pharmacy";
            }
        }, SHOPS {
            @Override
            public String getUrlQueryType() {
                return "shop";
            }
        }, ATMs {
            @Override
            public String getUrlQueryType() {
                return "atm";
            }
        };

        public abstract String getUrlQueryType();
    }

    String TAG = "tag";
    String BASE_URL = "http://overpass-api.de";

    String APPLICATION = "app";
    String APP_FIRST_START_PREFERENCE = "app_first_start";
    int APP_STARTED_FIRST_TIME = 0;
    int APP_STARTED_EVER = 1;
    int APP_STARTED_FIST_TIME_AFTER_UPDATE = 2;

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
    String CURRENT_CITY_ID_PREFERENCE = "city_id_preference";

    float TINY_ZOOM_LEVEL = 16.0f;
    float CITY_ZOOM_KOEF = 1.2f;

    double MIN_LAT_IF_AREA = 48.87;
    double MAX_LAT_IF_AREA = 48.94;
    double MIN_LON_IF_AREA = 24.75;
    double MAX_LON_IF_AREA = 24.69;

    int RECENT_BACK_PRESSED_TIME = 2000;
}
