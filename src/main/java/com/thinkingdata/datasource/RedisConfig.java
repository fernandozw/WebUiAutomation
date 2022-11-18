package com.thinkingdata.datasource;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/07/21 17:31
 */
@Configuration
public class RedisConfig {


    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    @Scope(value = "prototype")
    public GenericObjectPoolConfig redisPool() {
        return new GenericObjectPoolConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.redis0")
    public RedisStandaloneConfiguration redisConfig_0() {
        return new RedisStandaloneConfiguration();
    }


    @Primary
    @Bean
    public LettuceConnectionFactory factory_0(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfig_0) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfig_0, clientConfiguration);
    }


    @Bean(name = "redis_0")
    public StringRedisTemplate redis176Template(@Qualifier("factory_0") LettuceConnectionFactory factory_176) {
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factory_176);
        return template;
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.redis.redis1")
    public RedisStandaloneConfiguration redisConfig_01() {
        return new RedisStandaloneConfiguration();
    }

    @Bean
    public LettuceConnectionFactory factory_01(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfig_01) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfig_01, clientConfiguration);
    }


    @Bean(name = "redis_01")
    public StringRedisTemplate redis03Template(@Qualifier("factory_01") LettuceConnectionFactory factory_01) {
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factory_01);
        return template;
    }

    private StringRedisTemplate getRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
