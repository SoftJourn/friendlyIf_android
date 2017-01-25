package com.igorko.accesibleif.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Igorko on 23.01.2017.
 */

public class Element implements Parcelable {

    private String type;
    private long id;
    private double lat;
    private double lon;
    private Tags tags;
    private Bounds bounds;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Tags getTags() {
        return tags;
    }

    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeLong(this.id);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeParcelable(this.tags, flags);
        dest.writeParcelable(this.bounds, flags);
    }

    public Element() {
    }

    protected Element(Parcel in) {
        this.type = in.readString();
        this.id = in.readLong();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.tags = in.readParcelable(Tags.class.getClassLoader());
        this.bounds = in.readParcelable(Bounds.class.getClassLoader());
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel source) {
            return new Element(source);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };
}
