package xyz.jxzou.zblog.common.exception.model.exception;


import xyz.jxzou.zblog.common.core.model.Result;

import java.io.Serializable;

/**
 * ArgumentException
 *
 * @author jx
 */
public class ArgumentException extends BaseException implements Serializable {

    private static final long serialVersionUID = 6449334571737316648L;

    public ArgumentException(Result responseEnum, Object[] args) {
        super(responseEnum, args);
    }

    public ArgumentException(Result responseEnum, Object[] args, Throwable cause) {
        super(responseEnum, args, cause);
    }
}
