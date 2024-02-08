package xyz.jxzou.zblog.core.model.asserts;

import xyz.jxzou.zblog.core.model.exception.ArgumentException;
import xyz.jxzou.zblog.util.model.Result;

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
