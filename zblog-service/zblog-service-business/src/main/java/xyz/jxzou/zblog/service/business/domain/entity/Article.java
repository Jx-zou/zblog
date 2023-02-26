package xyz.jxzou.zblog.service.business.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@TableName(value = "article", schema = "business")
public class Article {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField
    private String name;
    @TableField
    private String title;
    @TableField
    private String desc;
    @TableField
    private String url;
    @TableField
    private Long author;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
