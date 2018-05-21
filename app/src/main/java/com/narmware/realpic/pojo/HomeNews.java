package com.narmware.realpic.pojo;

/**
 * Created by Ashwini Palve on 17/05/2018.
 */

public class HomeNews
{

    private String id;
    private String url;
    private String type;
    private String title;
    private String description;
    private String date_time;
    private String src;

    public HomeNews(String id, String url, String type, String title) {
        this.id = id;
        this.url = url;
        this.type = type;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getSrc() {
        return src;
    }
}
