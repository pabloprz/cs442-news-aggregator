package com.iit.pab.newsaggregator.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.iit.pab.newsaggregator.utils.DateTimeUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ArticleDTO implements Serializable, Parcelable {

    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private LocalDateTime publishedAt;

    public ArticleDTO(String author, String title, String description, String url,
                      String urlToImage, LocalDateTime publishedAt) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    protected ArticleDTO(Parcel in) {
        author = in.readString();
        title = in.readString();
        description = in.readString();
        url = in.readString();
        urlToImage = in.readString();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    // Parcelable implementation to save instance state
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeString(urlToImage);
        parcel.writeString(DateTimeUtils.formatDateTime(publishedAt));
    }

    public static final Creator<ArticleDTO> CREATOR = new Creator<ArticleDTO>() {
        @Override
        public ArticleDTO createFromParcel(Parcel in) {
            return new ArticleDTO(in);
        }

        @Override
        public ArticleDTO[] newArray(int size) {
            return new ArticleDTO[size];
        }
    };
}
