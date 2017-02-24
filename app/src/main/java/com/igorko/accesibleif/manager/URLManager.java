package com.igorko.accesibleif.manager;

import com.igorko.accesibleif.models.City;
import com.igorko.accesibleif.utils.Const;

/**
 * Created by Igorko on 16.02.2017.
 */

public class URLManager implements Const {

    public static String getBaseURL() {
        return BASE_URL;
    }

    public static String getDataQuery(BuildingsType type) {
        String dataQuery = "[out:json]";

        //////////////////// TODO
        // new - bad

        CityManager cityManager = new CityManager();
        dataQuery += getCityUrlPart(cityManager.getCurrentCity());
        dataQuery += getBuildingTypeUrlPart(type);
        return dataQuery;
    }

    private static String getCityUrlPart(City city) {
        return String.format("[bbox:%1$s,%2$s,%3$s,%4$s];",
                String.valueOf(city.getBottomRectPoint().latitude),
                String.valueOf(city.getBottomRectPoint().longitude),
                String.valueOf(city.getTopRectPoint().latitude),
                String.valueOf(city.getTopRectPoint().longitude));

    }

    private static String getBuildingTypeUrlPart(BuildingsType type) {
        if (!type.equals(BuildingsType.SHOPS)) {
            switch (type) {
                case ALL: {
                    return "(node[wheelchair];way[wheelchair];relation[wheelchair];);out geom;";
                }
                default:
                    return String.format("(node[wheelchair][\"amenity\"=\"%1$s\"];way[wheelchair][\"amenity\"=\"%1$s\"];relation[wheelchair][\"amenity\"=\"%1$s\"];); out geom;", type.getUrlQueryType());
            }
        } else {
            return String.format("(node[wheelchair][\"%1$s\"];way[wheelchair][\"%1$s\"];relation[wheelchair][\"%1$s\"];); out geom;", type.getUrlQueryType());
        }
    }
}
