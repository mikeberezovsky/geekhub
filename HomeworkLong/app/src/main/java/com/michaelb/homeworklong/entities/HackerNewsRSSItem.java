package com.michaelb.homeworklong.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelb on 11/12/14.
 */
public class HackerNewsRSSItem implements Parcelable {
    private String title;
    private String url;
    private String id;
    private String commentCount;
    private String points;
    private String postedAgo;
    private String postedBy;

    public HackerNewsRSSItem() {
    }

    public HackerNewsRSSItem(Parcel source) {
        title = source.readString();
        url = source.readString();
        id = source.readString();
        commentCount = source.readString();
        points = source.readString();
        postedAgo = source.readString();
        postedBy = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(id);
        dest.writeString(commentCount);
        dest.writeString(points);
        dest.writeString(postedAgo);
        dest.writeString(postedBy);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public class MyCreator implements Parcelable.Creator<HackerNewsRSSItem> {
        public HackerNewsRSSItem createFromParcel(Parcel source) {
            return new HackerNewsRSSItem(source);
        }
        public HackerNewsRSSItem[] newArray(int size) {
            return new HackerNewsRSSItem[size];
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

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPostedAgo() {
        return postedAgo;
    }

    public void setPostedAgo(String postedAgo) {
        this.postedAgo = postedAgo;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

}
