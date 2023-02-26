package xyz.jxzou.zblog.service.business.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@TableName(value = "user_article", schema = "business")
public class UserArticle {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField
    private Long uid;
    @TableField
    private Long aid;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
