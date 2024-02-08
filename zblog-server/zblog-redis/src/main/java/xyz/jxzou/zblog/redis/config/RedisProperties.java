package xyz.jxzou.zblog.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private Map<String, Integer> databases = defaultDatabases();
    private String host = "127.0.0.1";
    private Integer port = 6379;
    private String password;
    private Integer timeout = 3000;
    private Pool pool;

    @Data
    public static class Pool {
        private Integer maxActive = 100;
        private Integer maxIdle = 8;
        private Integer minIdle = 0;
        private Integer maxWait = -1;
    }

    private Map<String, Integer> defaultDatabases() {
        Map<String, Integer> defaultDatabases = new HashMap<>();
        defaultDatabases.put("default", 0);
        return defaultDatabases;
    }
}
