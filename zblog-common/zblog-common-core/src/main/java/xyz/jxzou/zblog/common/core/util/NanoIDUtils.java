package xyz.jxzou.zblog.common.core.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.util.Random;

public class NanoIDUtils {

    private static final String DEFAULT_SOURCE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random random = new Random();

    public static String alphanumericId() {
        return alphanumericId(18);
    }

    public static String alphanumericId(Integer length) {
        return NanoIdUtils.randomNanoId(random, DEFAULT_SOURCE.toCharArray(), length);
    }
}
