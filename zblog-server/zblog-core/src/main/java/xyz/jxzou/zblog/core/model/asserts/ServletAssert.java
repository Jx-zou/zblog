package xyz.jxzou.zblog.core.model.asserts;


import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import xyz.jxzou.zblog.core.pojo.exception.ServletException;
import xyz.jxzou.zblog.util.model.Result;

/**
 * ServletAssert
 *
 * @author jx
 */
public interface ServletAssert extends BaseAssert<ServletException>, Result {
    @Override
    default ServletException newException(Object... args) {
        return new ServletException(this, args);
    }

    @Override
    default ServletException newException(Throwable t, Object... args) {
        return new ServletException(this, args, t);
    }

    default void validateClientId(@Nullable String cid) throws ServletException {
        if (null == cid || cid.length() != 24) {
            throw this.newException();
        }
    }

    default void validateToken(@Nullable String token) throws ServletException {
        if (StringUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            throw this.newException();
        }
    }
}
