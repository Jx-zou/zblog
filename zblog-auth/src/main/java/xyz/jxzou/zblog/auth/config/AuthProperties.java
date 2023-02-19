package xyz.jxzou.zblog.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.jxzou.zblog.auth.domain.model.Client;
import xyz.jxzou.zblog.auth.domain.model.Jwt;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    private Client client;
    private Jwt jwt;
}
