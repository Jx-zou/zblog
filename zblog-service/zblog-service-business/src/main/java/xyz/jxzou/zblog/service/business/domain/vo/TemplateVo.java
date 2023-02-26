package xyz.jxzou.zblog.service.business.domain.vo;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Data
public class TemplateVo {

    @NotBlank
    private String name;
    @Digits(integer = 0, fraction = 0)
    private Integer type;
    @NotBlank
    private String content;
}
