package xyz.jxzou.zblog.service.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DurationUtils;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type User.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user", schema = "auth")
public class User {

    private static final long serialVersionUID = -1716323219834026200L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    @TableField(fill = FieldFill.INSERT, insertStrategy = FieldStrategy.NOT_EMPTY)
    private String account;

    /**
     * 密码
     */
    @JsonIgnore
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY)
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
    @TableField
    private String avatar;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 非锁定账户
     */
    @TableField
    private boolean accountNonLocked = false;

    /**
     * 账户不过期
     */
    @TableField
    private boolean accountNonExpired = false;

    /**
     * 凭证不过期
     */
    @TableField
    private boolean credentialsNonExpired = false;

    /**
     * 启用
     */
    @TableField
    private boolean enabled = false;
}
