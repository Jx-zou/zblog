package xyz.jxzou.zblog.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProp {

    private Map<String, Param> code;

    public Param getParam(String name) {
        return this.code.get(name);
    }

    @Data
    public static class Param {
        private Integer size;
        private Integer expire;
    }
}
