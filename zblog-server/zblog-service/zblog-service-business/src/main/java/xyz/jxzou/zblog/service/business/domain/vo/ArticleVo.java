package xyz.jxzou.zblog.service.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import xyz.jxzou.zblog.service.user.domain.entity.User;

import java.time.LocalDateTime;

@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ArticleVo {

    private Long id;
    private String title;
    private String profile;
    private String url;
    private String nickname;
    private String uprofile;
    private String avatar;
    private LocalDateTime createTime;
}
