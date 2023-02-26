package xyz.jxzou.zblog.auth.domain.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RUserVo {

    private String nickname;
    private String password;
    private String mail;
    private String desc;
    private String code;
}
