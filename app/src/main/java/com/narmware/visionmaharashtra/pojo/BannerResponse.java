package com.narmware.visionmaharashtra.pojo;

/**
 * Created by rohitsavant on 04/09/18.
 */

public class BannerResponse {
    String response;
    Banner[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Banner[] getData() {
        return data;
    }

    public void setData(Banner[] data) {
        this.data = data;
    }
}
