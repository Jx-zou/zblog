package xyz.jxzou.zblog.common.exception.enums;


import xyz.jxzou.zblog.common.core.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.model.asserts.ServletAssert;

/**
 * servlet响应枚举
 *
 * @author Jx
 */
public enum ServletResponseEnum implements ServletAssert {

    /**
     * Servlet错误: 5100 -> 5119
     */
    SERVLET_ERROR(5100, "网络延迟"),
    GET_PUBLICKEY_SUCCESS(5101, "获取公钥失败"),

    /**
     * client错误: 5120 -> 5139
     */
    CLIENT_ERROR(5120,"client错误"),
    CLIENT_ID_ERROR(5121,"clientID错误"),

    /**
     * 认证错误: 5140 -> 5159
     */
    AUTH_FAILED_ERROR(5140, "认证错误"),
    AUTH_LOGIN_ERROR(5141, "账户或者密码错误"),
    PERMISSION_DENIED_ERROR(5142, "权限不足"),

    /**
     * 秘钥错误: 5160 ->
     */
    RSA_GENERATOR_ERROR(5160, "RSA秘钥生成器错误"),
    RSA_STORE_PUT_ERROR(5161, "秘钥添加失败"),
    RSA_STORE_GET_ERROR(5162, "秘钥查询失败");



    private final Integer status;
    private final String message;

    ServletResponseEnum(Integer status, String message) {
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
