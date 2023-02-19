package xyz.jxzou.zblog.common.exception.enums;

import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.model.asserts.BusinessAssert;

/**
 * BusinessResponseEnum
 *
 * @author jx
 */
public enum BusinessResponseEnum implements BusinessAssert {
    MAIL_BOX_ERROR(5201,"无效邮箱"),
    MAIL_SEND_ERROR(5202,"邮件发送失败"),
    MAIL_RESEND_ERROR(5203,"验证码已存在"),
    CAPTCHA_ERROR(5204,"无效验证码"),
    REGISTRY_SUCCESS(2200, "注册成功"),
    REGISTRY_ERROR(5220, "注册失败"),
    REGISTRY_PASSWORD_ERROR(5222, "无效密码"),
    REGISTRY_AUTHORIZATION_ERROR(5223, "授权失败");


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
