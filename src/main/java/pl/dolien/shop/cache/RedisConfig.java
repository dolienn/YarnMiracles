package pl.dolien.shop.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableAutoConfiguration(exclude = {RedisRepositoriesAutoConfiguration.class})
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapperConfig objectMapperConfig;
    private final CacheConfigBuilder cacheConfigBuilder;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = cacheConfigBuilder.buildCacheConfig(objectMapperConfig.configureObjectMapper(false));
        RedisCacheConfiguration feedbackCacheConfig = cacheConfigBuilder.buildCacheConfig(objectMapperConfig.configureObjectMapper(true));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("feedbacksByProduct", feedbackCacheConfig);
        cacheConfigurations.put("default", defaultCacheConfig);

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}

