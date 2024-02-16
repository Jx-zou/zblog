package xyz.jxzou.zblog.auth.domain.pojo.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RUserVo {

    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
    private String desc;
}
