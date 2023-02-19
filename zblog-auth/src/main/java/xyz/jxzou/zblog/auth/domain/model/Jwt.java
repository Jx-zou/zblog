package xyz.jxzou.zblog.auth.domain.model;

import lombok.Data;

@Data
public class Jwt {

    private Long expire = 5L;
    private Token token;
}
