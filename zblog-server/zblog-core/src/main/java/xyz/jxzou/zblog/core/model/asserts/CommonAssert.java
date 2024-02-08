package xyz.jxzou.zblog.core.model.asserts;

import xyz.jxzou.zblog.core.model.exception.CommonException;
import xyz.jxzou.zblog.util.model.Result;

/**
 * CommonAssert
 *
 * @author jx
 */
public interface CommonAssert extends BaseAssert<CommonException>, Result {

    @Override
    default CommonException newException(Object... args) {
        return new CommonException(this, args);
    }

    @Override
    default CommonException newException(Throwable t, Object... args) {
        return new CommonException(this, args, t);
    }

    default void isInsertFailed(int insert) throws CommonException {
        if (insert <= 0) {
            throw this.newException();
        }
    }
}
