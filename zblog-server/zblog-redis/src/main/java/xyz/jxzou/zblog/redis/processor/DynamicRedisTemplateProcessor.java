package xyz.jxzou.zblog.redis.processor;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.redis.config.RedisProperties;

import java.time.Duration;
import java.util.Objects;

/**
 * The type Dynamic redis template.
 */
@Component
public class DynamicRedisTemplateProcessor implements BeanDefinitionRegistryPostProcessor {

    private final RedisProperties redisProperties;

    /**
     * Instantiates a new Dynamic redis template processor.
     *
     * @param redisProperties the redis properties
     */
    @Autowired
    public DynamicRedisTemplateProcessor(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
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

    @Override
    public void postProcessBeanDefinitionRegistry(@Nullable BeanDefinitionRegistry registry) throws BeansException {
        if (Objects.isNull(registry)) {
            throw new BeanCreationException("redisTemplate注册失败.");
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(StringRedisTemplate.class);
        redisProperties.getDatabases().forEach((name, index) -> {
            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            beanDefinition.setAttribute("connectionFactory", getRedisConnectionFactory(index));
            registry.registerBeanDefinition(name + "RedisTemplate", beanDefinition);
        });
    }

    @Override
    public void postProcessBeanFactory(@Nullable ConfigurableListableBeanFactory beanFactory) throws BeansException {}
}
