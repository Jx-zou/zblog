package xyz.jxzou.zblog.common.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.jxzou.zblog.common.core.pojo.RSAKeyPair;
import xyz.jxzou.zblog.common.core.pojo.ZBlogContent;
import xyz.jxzou.zblog.common.core.util.RSAUtils;
import xyz.jxzou.zblog.redis.processor.DynamicRedisTemplateProcessor;

@Configuration
@RequiredArgsConstructor
public class GlobalConfiguration {

    private final RsaProperties rsaProp;
    private final DynamicRedisTemplateProcessor redisTemplateProcessor;

    @Bean("rsaKeyPair")
    public RSAKeyPair rsaKeyPair() {
        return RSAUtils.generateRSAKeyPair(rsaProp.getSize());
    }

    @Bean("userRedisTemplate")
    public RedisTemplate<String, Object> userRedisTemplate() {
        return redisTemplateProcessor.getRedisTemplate(ZBlogContent.REDIS_DATABASE_USER_NAME);
    }

    @Bean("clientRedisTemplate")
    public RedisTemplate<String, Object> clientRedisTemplate() {
        return redisTemplateProcessor.getRedisTemplate(ZBlogContent.REDIS_DATABASE_CLIENT_NAME);
    }

    @Bean("captchaRedisTemplate")
    public RedisTemplate<String, Object> captchaRedisTemplate() {
        return redisTemplateProcessor.getRedisTemplate(ZBlogContent.REDIS_DATABASE_CAPTCHA_NAME);
    }

    @Bean("businessRedisTemplate")
    public RedisTemplate<String, Object> businessRedisTemplate() {
        return redisTemplateProcessor.getRedisTemplate(ZBlogContent.REDIS_DATABASE_BUSINESS_NAME);
    }
}
