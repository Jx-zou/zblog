package xyz.jxzou.zblog.core.pojo.exception;

import xyz.jxzou.zblog.util.model.Result;

import java.io.Serial;
import java.io.Serializable;

/**
 * CommonException
 *
 * @author jx
 */
public class CommonException extends BaseException implements Serializable {


    @Serial
    private static final long serialVersionUID = -7089426630610760987L;

    public CommonException(Result responseEnum, Object[] args) {
        super(responseEnum, args);
    }

    public CommonException(Result responseEnum, Object[] args, Throwable cause) {
        super(responseEnum, args, cause);
    }
}
