package cn.notemi.common;

/**
 * Header列表
 */
public class HeaderConstants {

	/**
	 * 用户的登录token
	 */
	public static final String X_AUTH_TOKEN = "x-auth-token";

	public static final String X_AUTH_USERNAME = "x-auth-username";

	public static final String X_AUTH_PASSWORD = "x-auth-password";

	public static final String X_AUTH_WECHAT_APP = "x-auth-wechat-app";
	/**
	 * api的版本号
	 */
	public static final String API_VERSION = "api-version";

	/**
	 * app版本号
	 */
	public static final String APP_VERSION = "app-version";

	/**
	 * 调用来源
	 */
	public static final String CALL_SOURCE = "call-source";

	/**
	 * API的返回格式 {@link cn.notemi.constant.ApiStyleEnum}
	 */
	public static final String API_STYLE = "api-style";
}