package xyz.jxzou.zblog.auth.domain.model;

import lombok.Data;

@Data
public class Token {

    private String header;
    private String iss;
    private String sub;
}
