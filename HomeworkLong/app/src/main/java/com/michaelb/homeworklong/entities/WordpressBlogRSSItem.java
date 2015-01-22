package com.michaelb.homeworklong.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by michaelb on 11/12/14.
 */
public class WordpressBlogRSSItem implements Parcelable {
    private long id;
    private String postId;
    private String title;
    private String url;
    private long datePublished;
    private String author;

    /* Inner class that defines the table contents */
    public static abstract class WordpressBlogRSSEntry implements BaseColumns {
        public static final String TABLE_NAME = "news_rss";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_POST_TITLE = "title";
        public static final String COLUMN_POST_URL = "url";
        public static final String COLUMN_POST_DATE_PUBLISHED = "date_published";
        public static final String COLUMN_POST_AUTHOR = "author";
    }


    public WordpressBlogRSSItem() {
    }

    public WordpressBlogRSSItem(Parcel source) {
        id = source.readLong();
        postId = source.readString();
        title = source.readString();
        url = source.readString();
        datePublished = source.readLong();
        author = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(postId);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Date getDatePublished() {
        return new Date(datePublished);
    }

    public long getDatePublishedTimestamp() {
        return datePublished;
    }

    public void setDatePublished(long timestamp) {
        this.datePublished = timestamp;
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
