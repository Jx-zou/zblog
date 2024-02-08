package xyz.jxzou.zblog.service.business.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.jxzou.zblog.common.util.pojo.Page;

@Data
@Configuration
@ConfigurationProperties(prefix = "article")
public class ArticleConfiguration {

    private Cron cron;
    private Integer pageSize = 10;
}
