package xyz.jxzou.zblog.core.model.exception;

import xyz.jxzou.zblog.util.model.Result;

import java.io.Serializable;

/**
 * ServletException
 *
 * @author jx
 */
public class ServletException extends BaseException implements Serializable {

    private static final long serialVersionUID = 6362564704155960407L;

    public ServletException(Result responseEnum, Object[] args) {
        super(responseEnum, args);
    }

    public ServletException(Result responseEnum, Object[] args, Throwable cause) {
        super(responseEnum, args, cause);
    }
}
