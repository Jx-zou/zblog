package xyz.jxzou.zblog.util.model;

import xyz.jxzou.zblog.util.enums.FileType;

import java.lang.annotation.*;

/**
 * The interface File check.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface FileCheck {

    /**
     * 校验失败提示语
     *
     * @return 提示语 string
     */
    String message() default "This file format is not supported";

    /**
     * 校验模式
     *
     * @return 校验模式类型
     */
    CheckType type() default CheckType.SUFFIX;

    /**
     * 校验后缀的所有类型
     *
     * @return 后缀字符串
     */
    String[] supportedSuffixes() default {};

    /**
     * 文件类型校验
     *
     * @return 文件类型
     */
    FileType[] supportedFileTypes() default {};

    /**
     * The enum Check type.
     */
    enum CheckType {
        /**
         * 文件后缀模式
         */
        SUFFIX,
        /**
         * 魔数校验模式
         */
        MAGIC_NUMBER,
        /**
         * 后缀与魔数模式
         */
        SUFFIX_MAGIC_NUMBER
    }
}
