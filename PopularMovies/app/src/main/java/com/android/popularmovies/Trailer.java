package com.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String name;
    private String key;

    public Trailer() {
    }

    public Trailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    protected Trailer(Parcel in) {
        name = in.readString();
        key = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }
}
