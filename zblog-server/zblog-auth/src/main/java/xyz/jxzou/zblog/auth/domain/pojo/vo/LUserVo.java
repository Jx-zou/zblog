package xyz.jxzou.zblog.auth.domain.pojo.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LUserVo {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
