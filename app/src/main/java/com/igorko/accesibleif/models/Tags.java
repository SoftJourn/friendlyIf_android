package com.igorko.accesibleif.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Igorko on 23.01.2017.
 */

public class Tags implements Parcelable {

    String automatic_door;
    String entrance;
    String exit;
    String railway;
    String wheelchair;
    String amenity;
    String name;
    String opening_hours;
    String shop;
    String aeroway;

    public String getRailway() {
        return railway;
    }

    public String getWheelchair() {
        return wheelchair;
    }

    public String getAmenity() {
        return amenity;
    }

    public String getName() {
        return name;
    }

    public String getShop() {
        return shop;
    }

    public String getAeroway() {
        return aeroway;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.automatic_door);
        dest.writeString(this.entrance);
        dest.writeString(this.exit);
        dest.writeString(this.railway);
        dest.writeString(this.wheelchair);
        dest.writeString(this.amenity);
        dest.writeString(this.name);
        dest.writeString(this.opening_hours);
        dest.writeString(this.shop);
        dest.writeString(this.aeroway);
    }

    public Tags() {
    }

    protected Tags(Parcel in) {
        this.automatic_door = in.readString();
        this.entrance = in.readString();
        this.exit = in.readString();
        this.wheelchair = in.readString();
        this.amenity = in.readString();
        this.name = in.readString();
        this.opening_hours = in.readString();
        this.shop = in.readString();
        this.aeroway = in.readString();
    }

    public static final Creator<Tags> CREATOR = new Creator<Tags>() {
        @Override
        public Tags createFromParcel(Parcel source) {
            return new Tags(source);
        }

        @Override
        public Tags[] newArray(int size) {
            return new Tags[size];
        }
    };
}
