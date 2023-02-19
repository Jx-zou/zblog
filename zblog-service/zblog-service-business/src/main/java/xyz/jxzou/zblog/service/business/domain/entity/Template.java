package xyz.jxzou.zblog.service.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
    @TableField
    private LocalDateTime createTime;
    @TableField
    private LocalDateTime updateTime;
}
