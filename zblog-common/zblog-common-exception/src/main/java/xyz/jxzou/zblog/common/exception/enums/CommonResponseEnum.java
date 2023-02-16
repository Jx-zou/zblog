package xyz.jxzou.zblog.common.exception.enums;


import xyz.jxzou.zblog.common.core.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.model.asserts.CommonAssert;

/**
 * CommonResponseEnum
 *
 * @author jx
 */
public enum CommonResponseEnum implements CommonAssert {

    SUCCESS(2000,"操作成功"),
    GET_SUCCESS(2100,"获取成功"),

    /**
     * 公共错误: 5000 -> 5099
     */
    SERVER_ERROR(5000, "服务错误"),
    INIT_ERROR(5100, "网络延迟");

    private final Integer status;
    private final String message;

    CommonResponseEnum(int status, String message) {
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
