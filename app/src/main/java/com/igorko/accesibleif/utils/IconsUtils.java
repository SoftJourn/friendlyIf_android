package com.igorko.accesibleif.utils;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.models.Data;

/**
 * Created by Igorko on 17.10.2016.
 */

public class IconsUtils implements Const {

    public static BitmapDescriptor setDefaultIcon(Data.Element element){
        //set wheelchar icon color
        String wheelchar = element.getTags().getWheelchair();
        int resid;
        switch (wheelchar) {
            case WHEELCHAIR_YES: {
                resid = R.mipmap.card_green_ic;
                break;
            }
            case WHEELCHAIR_NO: {
                resid = R.mipmap.card_red_ic;
                break;
            }
            case WHEELCHAIR_LIMIITED: {
                resid = R.mipmap.card_yellow_ic;
                break;
            }
            default: {
                resid = R.mipmap.card_black_ic;
                break;
            }
        }
        return BitmapDescriptorFactory.fromResource(resid);
    }

    public static BitmapDescriptor setHospitalIcon(Data.Element element){
        //set wheelchar icon color
        String wheelchar = element.getTags().getWheelchair();
        int resid;
        switch (wheelchar) {
            case WHEELCHAIR_YES: {
                resid = R.mipmap.hospital_green_ic;
                break;
            }
            case WHEELCHAIR_NO: {
                resid = R.mipmap.hospital_red_ic;
                break;
            }
            case WHEELCHAIR_LIMIITED: {
                resid = R.mipmap.hospital_yellow_ic;
                break;
            }
            default: {
                resid = R.mipmap.hospital_ic;
                break;
            }
        }
        return BitmapDescriptorFactory.fromResource(resid);
    }

    public static BitmapDescriptor setPharmacyIcon(Data.Element element){
        //set wheelchar icon color
        String wheelchar = element.getTags().getWheelchair();
        int resid;
        if (wheelchar != null && !wheelchar.isEmpty()){
            switch (wheelchar) {
                case WHEELCHAIR_YES: {
                    resid = R.mipmap.pharmacy_green_ic;
                    break;
                }
                case WHEELCHAIR_NO: {
                    resid = R.mipmap.pharmacy_red_ic;
                    break;
                }
                case WHEELCHAIR_LIMIITED: {
                    resid = R.mipmap.pharmacy_yellow_ic;
                    break;
                }
                default: {
                    resid = R.mipmap.pharmacy_ic;
                    break;
                }
            }
        }else {
            resid = R.mipmap.pharmacy_ic;
        }

        return BitmapDescriptorFactory.fromResource(resid);
    }

    public static BitmapDescriptor setRailwayIcon(Data.Element element){
        //set wheelchar icon color
        String wheelchar = element.getTags().getWheelchair();
        int resid;
        if (wheelchar != null && !wheelchar.isEmpty()){
            switch (wheelchar) {
                case WHEELCHAIR_YES: {
                    resid = R.mipmap.realway_green_icon;
                    break;
                }
                case WHEELCHAIR_NO: {
                    resid = R.mipmap.realway_red_icon;
                    break;
                }
                case WHEELCHAIR_LIMIITED: {
                    resid = R.mipmap.realway_yellow_icon;
                    break;
                }
                default: {
                    resid = R.mipmap.realway_icon;
                    break;
                }
            }
        }else {
            resid = R.mipmap.realway_icon;
        }

        return BitmapDescriptorFactory.fromResource(resid);
    }

    public static BitmapDescriptor setAirportIcon(Data.Element element){
        //set wheelchar icon color
        String wheelchar = element.getTags().getWheelchair();
        int resid;
        if (wheelchar != null && !wheelchar.isEmpty()){
            switch (wheelchar) {
                case WHEELCHAIR_YES: {
                    resid = R.mipmap.airport_green_ic;
                    break;
                }
                case WHEELCHAIR_NO: {
                    resid = R.mipmap.airport_red_ic;
                    break;
                }
                case WHEELCHAIR_LIMIITED: {
                    resid = R.mipmap.airport_yellow_ic;
                    break;
                }
                default: {
                    resid = R.mipmap.airport_ic;
                    break;
                }
            }
        }else {
            resid = R.mipmap.airport_ic;
        }

        return BitmapDescriptorFactory.fromResource(resid);
    }

    /*public static BitmapDescriptor setSmallIcon(Data.Element element){
        //set wheelchar icon color
        String wheelchar = element.getTags().getWheelchair();
        int resid;
        if (wheelchar != null && !wheelchar.isEmpty()){
            switch (wheelchar) {
                case WHEELCHAIR_YES: {
                    resid = R.mipmap.small_green_ic;
                    break;
                }
                case WHEELCHAIR_NO: {
                    resid = R.mipmap.small_red_ic;
                    break;
                }
                case WHEELCHAIR_LIMIITED: {
                    resid = R.mipmap.small_yellow_ic;
                    break;
                }
                default: {
                    resid = R.mipmap.small_black_ic;
                    break;
                }
            }
        }else {
            resid = R.mipmap.small_black_ic;
        }
        return BitmapDescriptorFactory.fromResource(resid);
    }*/

    public static BitmapDescriptor setTinyIcon(Data.Element element){
        //set wheelchar icon color
        String wheelchar = element.getTags().getWheelchair();
        int resid;
        if (wheelchar != null && !wheelchar.isEmpty()){
            switch (wheelchar) {
                case WHEELCHAIR_YES: {
                    resid = R.mipmap.tiny_green_ic;
                    break;
                }
                case WHEELCHAIR_NO: {
                    resid = R.mipmap.tiny_red_ic;
                    break;
                }
                case WHEELCHAIR_LIMIITED: {
                    resid = R.mipmap.tiny_yellow_ic;
                    break;
                }
                default: {
                    resid = R.mipmap.tiny_black_ic;
                    break;
                }
            }
        }else {
            resid = R.mipmap.tiny_black_ic;
        }
        return BitmapDescriptorFactory.fromResource(resid);
    }

    public static BitmapDescriptor setShopIcon(Data.Element element){
        //set wheelchar icon color
        String wheelchar = element.getTags().getWheelchair();
        int resid;
        switch (wheelchar) {
            case WHEELCHAIR_YES: {
                resid = R.mipmap.shop_green_ic;
                break;
            }
            case WHEELCHAIR_NO: {
                resid = R.mipmap.shop_red_ic;
                break;
            }
            case WHEELCHAIR_LIMIITED: {
                resid = R.mipmap.shop_yellow_ic;
                break;
            }
            default: {
                resid = R.mipmap.shop_ic;
                break;
            }
        }

        return BitmapDescriptorFactory.fromResource(resid);
    }
}
