package xyz.jxzou.zblog.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.jxzou.zblog.redis.processor.DynamicRedisTemplateProcessor;

@Configuration
public class RedisConfig {

//    private final DynamicRedisTemplateProcessor redisTemplateProcessor;
//
//    public RedisConfig(DynamicRedisTemplateProcessor redisTemplateProcessor) {
//        this.redisTemplateProcessor = redisTemplateProcessor;
//    }
//
//    @Bean("userRedisTemplate")
//    public RedisTemplate<String, Object> userRedisTemplate() {
//        return redisTemplateProcessor.getRedisTemplate(CoreContent.REDIS_DATABASE_NAME_USER);
//    }
//
//    @Bean("clientRedisTemplate")
//    public RedisTemplate<String, Object> clientRedisTemplate() {
//        return redisTemplateProcessor.getRedisTemplate(CoreContent.REDIS_DATABASE_NAME_CLIENT);
//    }
//
//    @Bean("captchaRedisTemplate")
//    public RedisTemplate<String, Object> captchaRedisTemplate() {
//        return redisTemplateProcessor.getRedisTemplate(CoreContent.REDIS_DATABASE_NAME_CAPTCHA);
//    }
//
//    @Bean("businessRedisTemplate")
//    public RedisTemplate<String, Object> businessRedisTemplate() {
//        return redisTemplateProcessor.getRedisTemplate(CoreContent.REDIS_DATABASE_NAME_BUSINESS);
//    }
}
