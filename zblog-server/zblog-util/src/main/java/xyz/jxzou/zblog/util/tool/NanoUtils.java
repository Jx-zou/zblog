package xyz.jxzou.zblog.util.tool;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.util.Random;

/**
 * Nano utils.
 */
public class NanoUtils {

    private static final int size = 24;
    private static final String DEFAULT_SOURCE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC_SOURCE = "0123456789";
    private static final String ALPHABET_SOURCE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random random = new Random();

    /**
     * Create alphanumeric string.
     *
     * @return alphanumeric string
     */
    public static String createAlphanumeric() {
        return createAlphanumeric(size);
    }

    /**
     * Create alphanumeric string.
     *
     * @param size size
     * @return alphanumeric string
     */
    public static String createAlphanumeric(int size) {
        return NanoIdUtils.randomNanoId(random, DEFAULT_SOURCE.toCharArray(), size);
    }

    /**
     * Create numeric string.
     *
     * @return numeric string
     */
    public static String createNumeric() {
        return createNumeric(size);
    }

    /**
     * Create numeric string.
     *
     * @param size size
     * @return numeric string
     */
    public static String createNumeric(int size) {
        return NanoIdUtils.randomNanoId(random, NUMERIC_SOURCE.toCharArray(), size);
    }

    /**
     * Create alphabet string.
     *
     * @return alphabet string
     */
    public static String createAlphabet() {
        return createAlphabet(size);
    }

    /**
     * Create alphabet string.
     *
     * @param size size
     * @return alphabet string
     */
    public static String createAlphabet(int size) {
        return NanoIdUtils.randomNanoId(random, ALPHABET_SOURCE.toCharArray(), size);
    }
}
