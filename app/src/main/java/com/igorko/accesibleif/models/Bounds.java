package com.igorko.accesibleif.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Igorko on 23.01.2017.
 */

public class Bounds implements Parcelable {
    private double minlat;
    private double minlon;
    private double maxlat;
    private double maxlon;

    public double getMinlat() {
        return minlat;
    }

    public void setMinlat(double minlat) {
        this.minlat = minlat;
    }

    public double getMinlon() {
        return minlon;
    }

    public void setMinlon(double minlon) {
        this.minlon = minlon;
    }

    public double getMaxlat() {
        return maxlat;
    }

    public void setMaxlat(double maxlat) {
        this.maxlat = maxlat;
    }

    public double getMaxlon() {
        return maxlon;
    }

    public void setMaxlon(double maxlon) {
        this.maxlon = maxlon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.minlat);
        dest.writeDouble(this.minlon);
        dest.writeDouble(this.maxlat);
        dest.writeDouble(this.maxlon);
    }

    public Bounds() {
    }

    protected Bounds(Parcel in) {
        this.minlat = in.readDouble();
        this.minlon = in.readDouble();
        this.maxlat = in.readDouble();
        this.maxlon = in.readDouble();
    }

    public static final Creator<Bounds> CREATOR = new Creator<Bounds>() {
        @Override
        public Bounds createFromParcel(Parcel source) {
            return new Bounds(source);
        }

        @Override
        public Bounds[] newArray(int size) {
            return new Bounds[size];
        }
    };
}