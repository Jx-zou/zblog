package xyz.jxzou.zblog.common.exception.model.asserts;


import xyz.jxzou.zblog.common.core.model.Result;
import xyz.jxzou.zblog.common.exception.model.exception.ServletException;

/**
 * ServletAssert
 *
 * @author jx
 */
public interface ServletAssert extends BaseAssert, Result {
    @Override
    default ServletException newException(Object... args) {
        return new ServletException(this, args);
    }

    @Override
    default ServletException newException(Throwable t, Object... args) {
        return new ServletException(this, args, t);
    }
}
