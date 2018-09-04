package com.narmware.visionmaharashtra.support;

/**
 * Created by rohitsavant on 03/09/18.
 */

public class Endpoint {
    private static final String BASE_URL = "http://letzlearn.in/vision/";
    public static final String GET_NEWS = BASE_URL + "get_videos_by_cat.php";
    public static final String GET_CATEGORIES = BASE_URL + "get_categories.php";
    public static final String LOGIN_USER = BASE_URL + "login.php";

    //variables
    public static final String CAT_ID = "cat_id";
    public static final String VIDEO_ID = "v_id";
    public static final String JSON_STRING = "json_string";

}
