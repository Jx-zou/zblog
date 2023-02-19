package xyz.jxzou.zblog.mail.config;

import lombok.Data;

@Data
public class Captcha {

    private Integer size = 6;
    private Integer expire = 60;
}
