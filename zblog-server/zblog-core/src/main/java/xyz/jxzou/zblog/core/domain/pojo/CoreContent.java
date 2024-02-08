package xyz.jxzou.zblog.core.domain.pojo;

public class CoreContent {

    /**
     * http header
     */
    public static final String HTTP_HEADER_CLIENT = "cid";
    public static final String HTTP_HEADER_PUBLICKEY = "pkey";
    public static final String HTTP_HEADER_ACCESS_TOKEN = "token";

    /**
     * 常规值
     */
    public static final String BASIC_ROLE = "user";
    public static final String BASIC_AVATAR = "/images/head.png";

    /**
     * redis相关
     */
    //redis过期
    public static final Long REDIS_CLIENT_EXPIRE = 10800L;

    //redis 前缀
    public static final String REDIS_PREFIX_CAPTCHA = "ZBLOG-CAPTCHA-";
    public static final String REDIS_PREFIX_CLIENT = "ZBLOG-CLIENT-";
    public static final String REDIS_PREFIX_USER = "ZBLOG-USER-";

    //redis 变量名
    public static final String REDIS_NAME_CLIENT_CREATE_TIME = "CLIENT-CREATE-TIME";
    public static final String REDIS_NAME_ARTICLE_PAGE = "ZBLOG-ARTICLE";
    public static final String REDIS_NAME_ARTICLE_TOTAL = "ARTICLE-TOTAL";

    //redis库名
    public static final String REDIS_DATABASE_NAME_CLIENT = "client";
    public static final String REDIS_DATABASE_NAME_USER = "user";
    public static final String REDIS_DATABASE_NAME_CAPTCHA = "captcha";
    public static final String REDIS_DATABASE_NAME_BUSINESS = "business";
}
