package xyz.jxzou.zblog.core.pojo.exception;

import xyz.jxzou.zblog.util.model.Result;

import java.io.Serial;
import java.io.Serializable;

/**
 * ArgumentException
 *
 * @author jx
 */
public class ArgumentException extends BaseException implements Serializable {

    @Serial
    private static final long serialVersionUID = 6449334571737316648L;

    public ArgumentException(Result responseEnum, Object[] args) {
        super(responseEnum, args);
    }

    public ArgumentException(Result responseEnum, Object[] args, Throwable cause) {
        super(responseEnum, args, cause);
    }
}
