package com.narmware.visionmaharashtra.pojo;

/**
 * Created by rohitsavant on 03/09/18.
 */

public class NewsItem {
    private String v_id, v_title, v_link, v_date;

    public NewsItem(String v_id, String v_title, String v_link, String v_date) {
        this.v_id = v_id;
        this.v_title = v_title;
        this.v_link = v_link;
        this.v_date = v_date;
    }

    public String getV_id() {
        return v_id;
    }

    public String getV_title() {
        return v_title;
    }

    public String getV_link() {
        return v_link;
    }

    public String getV_date() {
        return v_date;
    }
}
