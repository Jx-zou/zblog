package xyz.jxzou.zblog.service.auth.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * The type User.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(value = "user", schema = "auth")
public class User implements UserDetails {

    @Serial
    private static final long serialVersionUID = -1716323219834026200L;
    /**
     * id
     */
    @Id
    private Long id;
    /**
     * 密码
     */
    @JsonIgnore
    @Column
    private String secret;
    /**
     * 昵称
     */
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY)
    private String nickname;
    /**
     * 电话
     */
    @TableField
    private String phone;
    /**
     * 电子邮件
     */
    @TableField
    private String mail;
    /**
     * 简介
     */
    @TableField
    private String profile;
    /**
     * 头像
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String avatar;
    /**
     * 非锁定账户
     */
    @TableField(value = "account_non_locked")
    private Boolean accountNonLocked = true;
    /**
     * 账户不过期
     */
    @TableField(value = "account_non_expired")
    private Boolean accountNonExpired = true;
    /**
     * 凭证不过期
     */
    @TableField(value = "credentials_non_expired")
    private Boolean credentialsNonExpired = true;
    /**
     * 启用
     */
    @TableField(value = "enabled")
    private Boolean enabled = true;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
