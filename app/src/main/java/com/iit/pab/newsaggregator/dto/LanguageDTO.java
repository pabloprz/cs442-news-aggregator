package com.iit.pab.newsaggregator.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class LanguageDTO implements Comparable<LanguageDTO>, Parcelable {

    String code;
    String name;

    public LanguageDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    protected LanguageDTO(Parcel in) {
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
    public int compareTo(LanguageDTO languageDTO) {
        return this.getName().compareTo(languageDTO.getName());
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

    public static final Creator<LanguageDTO> CREATOR = new Creator<LanguageDTO>() {
        @Override
        public LanguageDTO createFromParcel(Parcel in) {
            return new LanguageDTO(in);
        }

        @Override
        public LanguageDTO[] newArray(int size) {
            return new LanguageDTO[size];
        }
    };
}
