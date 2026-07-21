//package com.melle.flightbooking.config;
//
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.data.web.config.SpringDataJacksonConfiguration;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableCaching
//public class RedisCacheConfig {
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        // 1. Create and configure Jackson ObjectMapper
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // CRUCIAL: Registers modules so Jackson understands how to read/write Spring Data Pages
//        objectMapper.registerModule(new SpringDataJacksonConfiguration.PageModule());
//        objectMapper.registerModule(new SpringDataJacksonConfiguration.SortModule());
//
//        // CRUCIAL: Saves type info into the JSON so Redis knows how to rebuild the PageImpl class on cache hits
//        objectMapper.activateDefaultTyping(
//                LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//
//        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//        // 2. Set up baseline configuration for all caches using Jackson JSON
//        RedisCacheConfiguration jsonConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));
//
//        // 3. Apply the specific 10-Minute TTL to your caches
//        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//        cacheConfigurations.put("flightCache", jsonConfig.entryTtl(Duration.ofMinutes(10)));
//        cacheConfigurations.put("bookingCache", jsonConfig.entryTtl(Duration.ofMinutes(10)));
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(jsonConfig)
//                .withInitialCacheConfigurations(cacheConfigurations)
//                .build();
//    }
//}
