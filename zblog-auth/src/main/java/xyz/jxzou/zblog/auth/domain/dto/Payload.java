package xyz.jxzou.zblog.auth.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Payload {

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
     * 过期时间
     */
    private Long exp;
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
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 电子邮件
     */
    private String mail;
    /**
     * desc
     */
    private String desc;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户角色列表
     */
    private List<String> roles;
}
