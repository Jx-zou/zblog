package xyz.jxzou.zblog.service.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
