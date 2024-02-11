package xyz.jxzou.zblog.core.pojo.enums;

import xyz.jxzou.zblog.core.model.asserts.ArgumentAssert;
import xyz.jxzou.zblog.util.pojo.ResponseResult;

/**
 * ArgumentResponseEnum
 *
 * @author jx
 */
public enum ArgumentResponseEnum implements ArgumentAssert {

    VALID_ERROR(5001,"参数错误"),
    CAPTCHA_ERROR(5002,"验证码错误");

    private final Integer status;
    private final String message;

    ArgumentResponseEnum(Integer status, String message) {
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
        return new ResponseResult<Void>(this.status, this.message);
    }
}
