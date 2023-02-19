package xyz.jxzou.zblog.common.exception.model.exception;

import xyz.jxzou.zblog.common.util.model.Result;

import java.io.Serializable;

/**
 * CommonException
 *
 * @author jx
 */
public class CommonException extends BaseException implements Serializable {


    private static final long serialVersionUID = -7089426630610760987L;

    public CommonException(Result responseEnum, Object[] args) {
        super(responseEnum, args);
    }

    public CommonException(Result responseEnum, Object[] args, Throwable cause) {
        super(responseEnum, args, cause);
    }
}
