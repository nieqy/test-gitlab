//package com.funshion.activity.common.redis;
//
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.springframework.boot.context.properties.bind.Bindable;
//import org.springframework.boot.context.properties.bind.Binder;
//import org.springframework.core.env.Environment;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.util.Map;
//
//public class RedisInit {
//
//
//    public RedisTemplate initRedisTemplate(Environment env, String redisPrefix) {
//        Binder binder = Binder.get(env);
//        Map config = binder.bind("spring.redis", Bindable.of(Map.class)).get();
//        int maxIdle = Integer.valueOf((String) config.get("maxIdle"));
//        int maxTotal = Integer.valueOf((String) config.get("maxTotal"));
//        int index = 0;
//        int maxWaitMillis = Integer.valueOf((String) config.get("maxWaitMillis"));
//        boolean testOnBorrow = Boolean.valueOf((String) config.get("testOnBorrow"));
//        Map hostMap = binder.bind(redisPrefix, Bindable.of(Map.class)).get();
//        String host = hostMap.get("host").toString();
//        int port = Integer.parseInt(hostMap.get("port").toString());
//
//        RedisTemplate redisTemplate = new RedisTemplate();
//        redisTemplate.setConnectionFactory(connectionFactory(host, port, maxIdle, maxTotal, index, maxWaitMillis, testOnBorrow));
//
//        FastJsonRedisSerializer defaultSerializer = new FastJsonRedisSerializer(Object.class);
//        RedisSerializer keyRedisSerializer = new StringRedisSerializer();
//        //Jackson2JsonRedisSerializer valueRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        redisTemplate.setKeySerializer(keyRedisSerializer);
//        redisTemplate.setValueSerializer(defaultSerializer);
//        redisTemplate.setHashKeySerializer(keyRedisSerializer);
//        redisTemplate.setHashValueSerializer(defaultSerializer);
//        redisTemplate.setDefaultSerializer(defaultSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//
//
//    private LettuceConnectionFactory connectionFactory(String hostName, int port, int maxIdle,
//                                                       int maxTotal, int index, long maxWaitMillis, boolean testOnBorrow) {
//
//        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//        poolConfig.setMaxIdle(maxIdle);
//        poolConfig.setMaxTotal(maxTotal);
//        poolConfig.setMaxWaitMillis(maxWaitMillis);
//        poolConfig.setTestOnBorrow(testOnBorrow);
//        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
//                .poolConfig(poolConfig)
//                .build();
//
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, port);
//        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
//    }
//
//}
