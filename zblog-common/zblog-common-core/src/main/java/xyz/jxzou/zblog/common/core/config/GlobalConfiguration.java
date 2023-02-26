package xyz.jxzou.zblog.common.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.jxzou.zblog.common.core.domain.pojo.CommonContent;
import xyz.jxzou.zblog.redis.processor.DynamicRedisTemplateProcessor;

@Getter
@Configuration
@RequiredArgsConstructor
public class GlobalConfiguration {

    private final RsaProp rsa;
    private final JwtProp jwt;
    private final CaptchaProp captcha;
    private final DynamicRedisTemplateProcessor redisTemplateProcessor;

    @Bean("userRedisTemplate")
    public RedisTemplate<String, Object> userRedisTemplate() {
        return redisTemplateProcessor.getRedisTemplate(CommonContent.REDIS_DATABASE_NAME_USER);
    }

    @Bean("clientRedisTemplate")
    public RedisTemplate<String, Object> clientRedisTemplate() {
        return redisTemplateProcessor.getRedisTemplate(CommonContent.REDIS_DATABASE_NAME_CLIENT);
    }

    @Bean("captchaRedisTemplate")
    public RedisTemplate<String, Object> captchaRedisTemplate() {
        return redisTemplateProcessor.getRedisTemplate(CommonContent.REDIS_DATABASE_NAME_CAPTCHA);
    }

    @Bean("businessRedisTemplate")
    public RedisTemplate<String, Object> businessRedisTemplate() {
        return redisTemplateProcessor.getRedisTemplate(CommonContent.REDIS_DATABASE_NAME_BUSINESS);
    }
}
