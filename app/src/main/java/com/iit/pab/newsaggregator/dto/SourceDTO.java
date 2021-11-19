package com.iit.pab.newsaggregator.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SourceDTO implements Serializable, Parcelable {

    private String id;
    private String name;
    private String category;
    private String language;
    private String country;

    public SourceDTO(String id, String name, String category, String language, String country) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.language = language;
        this.country = country;
    }

    protected SourceDTO(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
        language = in.readString();
        country = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Parcelable implementation to save instance state
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(category);
        parcel.writeString(language);
        parcel.writeString(country);
    }

    public static final Creator<SourceDTO> CREATOR = new Creator<SourceDTO>() {
        @Override
        public SourceDTO createFromParcel(Parcel in) {
            return new SourceDTO(in);
        }

        @Override
        public SourceDTO[] newArray(int size) {
            return new SourceDTO[size];
        }
    };
}
