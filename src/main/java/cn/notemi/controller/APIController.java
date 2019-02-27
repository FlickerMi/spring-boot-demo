package cn.notemi.controller;

/**
 * Title：APIController
 *
 * @author Flicker
 * @create 2018/4/18 0018 11:41
 **/
public class APIController {
    public static final String BASE_URL = "/api";
    public static final String FAVICON_URL = "/favicon.ico";
    public static final String AUTH_URL = BASE_URL + "/auth";
    public static final String AUTH_LOCAL_URL = AUTH_URL + "/local";
    public static final String ACCOUNT_URL = BASE_URL + "/account";
    public static final String DRUID_URL = "/druid";
    public static final String SWAGGER_API_URL = "/v2/api-docs";
    public static final String SWAGGER_UI_URL = "/swagger-ui.html";
    public static final String SWAGGER_RESOURCES_URL = "/swagger-resources";
    public static final String SWAGGER_WEBJARS_URL = "/webjars";

    public static String[] getNoAuthGetUrls() {
        return new String[]{
            AUTH_URL,
            FAVICON_URL,
            DRUID_URL,
            DRUID_URL + "/*",
            DRUID_URL + "/**",
            SWAGGER_API_URL,
            SWAGGER_UI_URL,
            SWAGGER_UI_URL + "/*",
            SWAGGER_RESOURCES_URL + "/**",
                SWAGGER_WEBJARS_URL + "/**",
        };
    }

    public static String[] getNoAuthPostUrls() {
        return new String[]{
            AUTH_URL,
            AUTH_URL + "/action/activate",
            DRUID_URL,
            DRUID_URL + "/*",
            SWAGGER_UI_URL,
            SWAGGER_API_URL
        };
    }
}
