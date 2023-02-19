package xyz.jxzou.zblog.common.exception.model.asserts;

import xyz.jxzou.zblog.common.util.model.Result;
import xyz.jxzou.zblog.common.exception.model.exception.CommonException;

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
