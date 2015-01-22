package com.michaelb.homeworklong.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by michaelb on 11/12/14.
 */
public class WordpressBlogPost implements Parcelable {
    private String id;
    private String postId;
    private String title;
    private String url;
    private String postHTML;
    private String postGzipped;

    /* Inner class that defines the table contents */
    public static abstract class WordpressBlogPostEntry implements BaseColumns {
        public static final String TABLE_NAME = "blog_post";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_POST_TITLE = "title";
        public static final String COLUMN_POST_URL = "url";
        public static final String COLUMN_POST_HTML = "post_html";
        public static final String COLUMN_POST_GZIPPED = "post_gzipped";
    }


    public WordpressBlogPost() {
    }

    public WordpressBlogPost(Parcel source) {
        id = source.readString();
        postId = source.readString();
        title = source.readString();
        url = source.readString();
        postHTML = source.readString();
        postGzipped = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(postId);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(postHTML);
        dest.writeString(postGzipped);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public class MyCreator implements Creator<WordpressBlogPost> {
        public WordpressBlogPost createFromParcel(Parcel source) {
            return new WordpressBlogPost(source);
        }
        public WordpressBlogPost[] newArray(int size) {
            return new WordpressBlogPost[size];
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostHTML() {
        return postHTML;
    }

    public void setPostHTML(String postHTML) {
        this.postHTML = postHTML;
    }

    public String getPostGzipped() {
        return postGzipped;
    }

    public void setPostGzipped(String postGzipped) {
        this.postGzipped = postGzipped;
    }

}
