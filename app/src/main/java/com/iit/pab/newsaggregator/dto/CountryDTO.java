package com.iit.pab.newsaggregator.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryDTO implements Comparable<CountryDTO>, Parcelable {

    String code;
    String name;

    public CountryDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    protected CountryDTO(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(CountryDTO countryDTO) {
        if (countryDTO != null && this.getName() != null && countryDTO.getName() != null) {
            return this.getName().compareTo(countryDTO.getName());
        }
        return 0;
    }

    // Parcelable implementation to save instance state
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(name);
    }

    public static final Creator<CountryDTO> CREATOR = new Creator<CountryDTO>() {
        @Override
        public CountryDTO createFromParcel(Parcel in) {
            return new CountryDTO(in);
        }

        @Override
        public CountryDTO[] newArray(int size) {
            return new CountryDTO[size];
        }
    };
}
