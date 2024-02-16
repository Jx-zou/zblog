package xyz.jxzou.zblog.service.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "permission", schema = "auth")
public class Permission {

    private static final long serialVersionUID = 1850172557451036958L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField
    private String name;

    @TableField
    private String url;

    @TableField
    private String profile;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
