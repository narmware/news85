package com.narmware.realpic.pojo;

/**
 * Created by Ashwini Palve on 16/05/2018.
 */

public class HomePojoResponse
{

     String response;
    HomeNews[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public HomeNews[] getData() {
        return data;
    }

    public void setData(HomeNews[] data) {
        this.data = data;
    }
}
