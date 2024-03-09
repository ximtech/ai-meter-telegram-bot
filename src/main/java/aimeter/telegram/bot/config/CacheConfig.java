package aimeter.telegram.bot.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    public static final int INITIAL_CACHE_CAPACITY = 1000;
    public static final int MAXIMUM_CACHE_SIZE = 5000;
    public static final Duration ENTRY_EXPIRATION_TIME = Duration.ofDays(30);

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("userLang");
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .initialCapacity(INITIAL_CACHE_CAPACITY)
                .maximumSize(MAXIMUM_CACHE_SIZE)
                .expireAfterWrite(ENTRY_EXPIRATION_TIME);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
