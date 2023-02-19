package xyz.jxzou.zblog.service.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "role_permission", schema = "auth")
public class RolePermission {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    @TableField
    private Long rid;
    @TableField
    private Long pid;
    @TableField
    private LocalDateTime createTime;
    @TableField
    private LocalDateTime updateTime;
}
