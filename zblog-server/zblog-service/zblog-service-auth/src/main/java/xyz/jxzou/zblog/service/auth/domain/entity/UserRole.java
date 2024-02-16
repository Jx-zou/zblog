package xyz.jxzou.zblog.service.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user_role", schema = "auth")
public class UserRole {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    @TableField
    private Long uid;
    @TableField
    private Long rid;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
