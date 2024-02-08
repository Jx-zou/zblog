package xyz.jxzou.zblog.auth.domain.pojo.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserinfoVo {

    @NotBlank
    private String nickname;

    private String desc;
}
