package cn.notemi.controller;

/**
 * Title：APIController
 *
 * @author Flicker
 * @create 2018/4/18 0018 11:41
 **/
public class APIController {
    public static final String BASE_URL = "/api";
    public static final String AUTH_URL = BASE_URL + "/auth";
    public static final String AUTH_LOCAL_URL = AUTH_URL + "/local";
    public static final String ACCOUNT_URL = BASE_URL + "/account";

    public static String[] getNoAuthGetUrls() {
        return new String[]{
            AUTH_URL,
            ACCOUNT_URL + "/*"
        };
    }

    public static String[] getNoAuthPostUrls() {
        return new String[]{
            ACCOUNT_URL,
            AUTH_URL,
            AUTH_URL + "/action/activate",
        };
    }
}
