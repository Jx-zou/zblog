package xyz.jxzou.zblog.service.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "role", schema = "auth")
public class Role {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField
    private String name;

    @TableField
    private String profile;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
