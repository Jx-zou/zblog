package xyz.jxzou.zblog.common.util.annotation;

import xyz.jxzou.zblog.common.util.enums.FileType;

import java.lang.annotation.*;


/**
 * The interface File check.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface FileCheck {
    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "不支持的文件格式";

    /**
     * Type check type.
     *
     * @return the check type
     */
    CheckType type() default CheckType.SUFFIX;

    /**
     * Supported suffixes string [ ].
     *
     * @return the string [ ]
     */
    String[] supportedSuffixes() default {};

    /**
     * Supported file types file type [ ].
     *
     * @return the file type [ ]
     */
    FileType[] supportedFileTypes() default {};

    /**
     * The enum Check type.
     */
    enum CheckType {
        /**
         * Suffix check type.
         */
        SUFFIX,
        /**
         * Magic number check type.
         */
        MAGIC_NUMBER,
        /**
         * Suffix magic number check type.
         */
        SUFFIX_MAGIC_NUMBER
    }
}


