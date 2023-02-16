package xyz.jxzou.zblog.common.exception.model.asserts;


import xyz.jxzou.zblog.common.core.model.Result;
import xyz.jxzou.zblog.common.exception.model.exception.BusinessException;

/**
 * BusinessAssert
 *
 * @author jx
 */
public interface BusinessAssert extends BaseAssert, Result {

    @Override
    default BusinessException newException(Object... args) {
        return new BusinessException(this, args);
    }

    @Override
    default BusinessException newException(Throwable t, Object... args) {
        return new BusinessException(this, args, t);
    }
}
