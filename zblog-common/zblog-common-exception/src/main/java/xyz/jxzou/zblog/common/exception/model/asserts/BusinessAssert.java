package xyz.jxzou.zblog.common.exception.model.asserts;


import xyz.jxzou.zblog.common.util.model.Result;
import xyz.jxzou.zblog.common.util.tool.ValidatorUtils;
import xyz.jxzou.zblog.common.exception.model.exception.BusinessException;

/**
 * BusinessAssert
 *
 * @author jx
 */
public interface BusinessAssert extends BaseAssert<BusinessException>, Result {

    @Override
    default BusinessException newException(Object... args) {
        return new BusinessException(this, args);
    }

    @Override
    default BusinessException newException(Throwable t, Object... args) {
        return new BusinessException(this, args, t);
    }

    default void validateCaptcha(String captcha, int size) throws BusinessException {
        if (!ValidatorUtils.isMailCaptcha(captcha) || captcha.length() != size) {
            throw this.newException();
        }
    }

    default void validateMail(String mail) throws BusinessException {
        if (!ValidatorUtils.isEmail(mail)) {
            throw this.newException();
        }
    }

    default void validatePassword(String password) throws BusinessException {
        if (!ValidatorUtils.isPassword(password)) {
            throw this.newException();
        }
    }
}
