package xyz.jxzou.zblog.common.exception.enums;

import xyz.jxzou.zblog.common.core.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.model.asserts.BusinessAssert;

/**
 * BusinessResponseEnum
 *
 * @author jx
 */
public enum BusinessResponseEnum implements BusinessAssert {
    USERNAME_NOT_FOUND(40500,"该用户不存在");

    private final Integer status;
    private final String message;

    BusinessResponseEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public Integer getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public ResponseResult<Void> getResult() {
        return new ResponseResult<Void>(status, message);
    }
}
