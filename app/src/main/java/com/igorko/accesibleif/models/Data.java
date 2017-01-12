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

    public static class Bounds implements Parcelable {
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

    public static class Element implements Parcelable {
        private String type;
        private long id;
        private double lat;
        private double lon;
        private Tags tags;
        private Data.Bounds bounds;

        public static class Tags implements Parcelable {
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
