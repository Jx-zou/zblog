package xyz.jxzou.zblog.auth.domain.pojo.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class JwtPayload {

    /**
     * 发行人
     */
    private String iss;
    /**
     * 主题
     */
    private String sub;
    /**
     * 签发时间
     */
    private Long iat;
    /**
     * JWT ID
     */
    private String jti;
    /**
     * 客户端加密ID
     */
    private String cid;
    /**
     * 用户名
     */
    private String account;
}
