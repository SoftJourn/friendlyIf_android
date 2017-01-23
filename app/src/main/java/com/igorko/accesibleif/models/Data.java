package com.igorko.accesibleif.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by Igorko on 17.10.2016.
 */

public class Data implements Parcelable {
    @SerializedName("elements")
    ArrayList<Element> elements ;

    public ArrayList<Element> getElements() {
        return elements;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.elements);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.elements = new ArrayList<Element>();
        in.readList(this.elements, Element.class.getClassLoader());
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
