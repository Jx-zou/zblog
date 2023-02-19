package xyz.jxzou.zblog.common.util.tool;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * The type Cookie utils.
 */
@Slf4j
public class CookieUtils {

    /**
     * Get string.
     *
     * @param request the request
     * @param key     the key
     * @return the string
     */
    public static String get(HttpServletRequest request, String key) {
        return doGet(request, key, false, "utf-8");
    }

    /**
     * Get string.
     *
     * @param request  the request
     * @param key      the key
     * @param isDecode the is decode
     * @return the string
     */
    public static String get(HttpServletRequest request, String key, boolean isDecode) {
        return doGet(request, key, isDecode, "utf-8");
    }

    /**
     * Get string.
     *
     * @param request the request
     * @param key     the key
     * @param enc     the enc
     * @return the string
     */
    public static String get(HttpServletRequest request, String key, String enc) {
        return doGet(request, key, true, enc);
    }

    private static String doGet(HttpServletRequest request, String key, boolean isDecode, String enc) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length < 1 || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return isDecode ? URLDecoder.decode(cookie.getValue(), enc) : cookie.getValue();
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Cookie decode Error.", e);
        }
        return null;
    }

}
