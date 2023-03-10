package xyz.jxzou.zblog.common.exception.model.asserts;

import xyz.jxzou.zblog.common.util.model.Result;
import xyz.jxzou.zblog.common.exception.model.exception.ArgumentException;

/**
 * ArgumentAssert
 *
 * @author jx
 */
public interface ArgumentAssert extends BaseAssert<ArgumentException>, Result {
    @Override
    default ArgumentException newException(Object... args) {
        return new ArgumentException(this, args);
    }

    @Override
    default ArgumentException newException(Throwable t, Object... args) {
        return new ArgumentException(this, args, t);
    }
}
