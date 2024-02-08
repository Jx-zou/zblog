package xyz.jxzou.zblog.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@RequiredArgsConstructor
public class GlobalConfiguration {

    private final RsaProp rsa;
    private final JwtProp jwt;
    private final CaptchaProp captcha;

}
