package xyz.jxzou.zblog.service.business.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@TableName(value = "template", schema = "business")
public class Template {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField
    private String name;
    @TableField
    private Integer type;
    @TableField
    private String content;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
