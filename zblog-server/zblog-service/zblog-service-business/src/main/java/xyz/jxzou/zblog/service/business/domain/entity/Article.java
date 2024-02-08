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
    @TableField(value = "name")
    private String name;
    @TableField(value = "title")
    private String title;
    @TableField(value = "profile")
    private String profile;
    @TableField(value = "url")
    private String url;
    @TableField(value = "author")
    private Long author;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
