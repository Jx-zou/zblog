package xyz.jxzou.zblog.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String header;

    private String base64Secret;

    private Long tokenValidityInSeconds;
}
