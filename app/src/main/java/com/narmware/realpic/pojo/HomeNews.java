package com.narmware.realpic.pojo;

/**
 * Created by Ashwini Palve on 17/05/2018.
 */

public class HomeNews
{

    private String id;
    private String image_url;
    private String news_url;
    private String type;
    private String title;
    private String description;
    private String date_time;
    private String src;

    public HomeNews(String id, String url, String type, String title) {
        this.id = id;
        this.image_url = url;
        this.type = type;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String url) {
        this.image_url = url;
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

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }
}
