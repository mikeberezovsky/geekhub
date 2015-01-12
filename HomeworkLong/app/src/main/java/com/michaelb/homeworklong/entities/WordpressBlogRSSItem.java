package com.michaelb.homeworklong.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by michaelb on 11/12/14.
 */
public class WordpressBlogRSSItem implements Parcelable {
    private String id;
    private String title;
    private String url;
    private long datePublished;
    private String author;

    public WordpressBlogRSSItem() {
    }

    public WordpressBlogRSSItem(Parcel source) {
        id = source.readString();
        title = source.readString();
        url = source.readString();
        datePublished = source.readLong();
        author = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeLong(datePublished);
        dest.writeString(author);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public class MyCreator implements Creator<WordpressBlogRSSItem> {
        public WordpressBlogRSSItem createFromParcel(Parcel source) {
            return new WordpressBlogRSSItem(source);
        }
        public WordpressBlogRSSItem[] newArray(int size) {
            return new WordpressBlogRSSItem[size];
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDatePublished() {
        return new Date(datePublished);
    }

    public long getDatePublishedTimestamp() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        if (datePublished != null) {
            this.datePublished = datePublished.getTime();
        } else {
            this.datePublished = 0;
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
