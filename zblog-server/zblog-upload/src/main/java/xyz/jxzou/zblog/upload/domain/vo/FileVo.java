package xyz.jxzou.zblog.upload.domain.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileVo {

    private String name;
    private String url;
}
