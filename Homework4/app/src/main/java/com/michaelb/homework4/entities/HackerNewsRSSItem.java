package com.michaelb.homework4.entities;

/**
 * Created by michaelb on 11/12/14.
 */
public class HackerNewsRSSItem {
    private String title;
    private String url;
    private String id;
    private String commentCount;
    private String points;
    private String postedAgo;
    private String postedBy;

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
