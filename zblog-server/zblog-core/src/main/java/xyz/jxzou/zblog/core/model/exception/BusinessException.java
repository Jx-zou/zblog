package xyz.jxzou.zblog.core.model.exception;


import xyz.jxzou.zblog.util.model.Result;

import java.io.Serializable;

/**
 * BusinessException
 *
 * @author jx
 */
public class BusinessException extends BaseException implements Serializable {

    private static final long serialVersionUID = 7373277090924206609L;

    public BusinessException(Result responseEnum, Object[] args) {
        super(responseEnum, args);
    }

    public BusinessException(Result responseEnum, Object[] args, Throwable cause) {
        super(responseEnum, args, cause);
    }
}
