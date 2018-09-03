package com.narmware.visionmaharashtra.pojo;

/**
 * Created by savvy on 12/7/2017.
 */

public class VideoPojo2 {
    String url;
    String id;

    public VideoPojo2(String id, String url) {
        this.url = url;
        this.id = id;
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

    public static String getVideoId(String url) {
        return url.substring(32,url.length());
    }
}


