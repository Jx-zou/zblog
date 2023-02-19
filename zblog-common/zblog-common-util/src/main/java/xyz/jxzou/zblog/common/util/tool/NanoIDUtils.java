package xyz.jxzou.zblog.common.util.tool;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.util.Random;

public class NanoIDUtils {

    private static final int size = 24;
    private static final String DEFAULT_SOURCE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC_SOURCE = "0123456789";
    private static final String ALPHABET_SOURCE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random random = new Random();

    public static String alphanumericId() {
        return alphanumericId(size);
    }

    public static String alphanumericId(Integer length) {
        return NanoIdUtils.randomNanoId(random, DEFAULT_SOURCE.toCharArray(), length);
    }

    public static String numericId() {
        return numericId(size);
    }

    public static String numericId(Integer length) {
        return NanoIdUtils.randomNanoId(random, NUMERIC_SOURCE.toCharArray(), length);
    }

    public static String alphabetId() {
        return alphanumericId(size);
    }

    public static String alphabetId(Integer length) {
        return NanoIdUtils.randomNanoId(random, ALPHABET_SOURCE.toCharArray(), length);
    }
}
