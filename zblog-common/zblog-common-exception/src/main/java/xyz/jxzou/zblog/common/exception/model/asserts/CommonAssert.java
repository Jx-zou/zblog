package xyz.jxzou.zblog.common.exception.model.asserts;

import xyz.jxzou.zblog.common.core.model.Result;
import xyz.jxzou.zblog.common.exception.model.exception.CommonException;

/**
 * CommonAssert
 *
 * @author jx
 */
public interface CommonAssert extends BaseAssert, Result {

    @Override
    default CommonException newException(Object... args) {
        return new CommonException(this, args);
    }

    @Override
    default CommonException newException(Throwable t, Object... args) {
        return new CommonException(this, args, t);
    }
}
