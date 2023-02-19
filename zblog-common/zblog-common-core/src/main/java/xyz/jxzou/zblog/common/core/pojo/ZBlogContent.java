package xyz.jxzou.zblog.common.core.pojo;

public class ZBlogContent {

    public static final String CLIENT_HEADER_NAME = "cid";
    public static final String PUBLICKEY_HEADER_NAME = "pkey";
    public static final String CLIENT_REDIS_CREATE_TIME_NAME = "CLIENT-CREATE-TIME";
    public static final String CAPTCHA_REDIS_PREFIX = "CAPTCHA-";
    public static final String DEFAULT_USER_ROLE_NAME = "user";

    /**
     * redis库名
     */
    public static final String REDIS_DATABASE_CLIENT_NAME = "client";
    public static final String REDIS_DATABASE_USER_NAME = "user";
    public static final String REDIS_DATABASE_CAPTCHA_NAME = "captcha";
    public static final String REDIS_DATABASE_BUSINESS_NAME = "business";
}
