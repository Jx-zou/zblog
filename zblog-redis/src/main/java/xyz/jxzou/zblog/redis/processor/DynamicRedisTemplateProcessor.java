package xyz.jxzou.zblog.redis.processor;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.redis.config.RedisProperties;
import xyz.jxzou.zblog.redis.pojo.FastJson2JsonRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Dynamic redis template.
 */

@Component
public class DynamicRedisTemplateProcessor {

    private Map<String, RedisTemplate<String, Object>> redisTemplateMap;
    private final RedisProperties redisProperties;

    @Autowired
    public DynamicRedisTemplateProcessor(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
        initialize();
    }

    private void initialize() {
        redisTemplateMap = new ConcurrentHashMap<>();
        FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
        redisProperties.getDatabases().forEach((name, index) -> {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(getRedisConnectionFactory(index));
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);
            redisTemplate.afterPropertiesSet();
            redisTemplateMap.put(name, redisTemplate);
        });
    }

    private GenericObjectPoolConfig<Object> getGenericObjectPoolConfig() {
        GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        RedisProperties.Pool pool = redisProperties.getPool();
        genericObjectPoolConfig.setMaxIdle(pool.getMaxIdle());
        genericObjectPoolConfig.setMinIdle(pool.getMinIdle());
        genericObjectPoolConfig.setMaxTotal(pool.getMaxActive());
        genericObjectPoolConfig.setMaxWait(Duration.ofMillis(pool.getMaxWait()));
        return genericObjectPoolConfig;
    }

    private RedisStandaloneConfiguration getRedisStandaloneConfiguration(Integer database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        return redisStandaloneConfiguration;
    }

    private LettuceConnectionFactory getRedisConnectionFactory(Integer database) {
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(getGenericObjectPoolConfig()).build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(getRedisStandaloneConfiguration(database), lettucePoolingClientConfiguration);
        factory.afterPropertiesSet();
        return factory;
    }

    public RedisTemplate<String, Object> getRedisTemplate(String name) {
        return redisTemplateMap.get(name);
    }
}
