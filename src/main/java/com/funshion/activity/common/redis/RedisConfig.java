package com.funshion.activity.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
public class RedisConfig implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    private DefaultListableBeanFactory defaultListableBeanFactory;

    private Environment env;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        redisRegistry(registry);
    }

    public void redisRegistry(BeanDefinitionRegistry registry) {
        String redisNames = Binder.get(env).bind("spring.redis.names", Bindable.of(String.class)).get();
        for (String name : StringUtils.split(redisNames, ",")) {
            RedisTemplate redisTemplate = initRedisTemplate(env, "spring.redis." + name);
            String beanName = "redisTemplate" + name;
            defaultListableBeanFactory.registerSingleton(beanName, redisTemplate);
            if (!defaultListableBeanFactory.containsBean("redisTemplate")) {
                defaultListableBeanFactory.registerAlias(beanName, "redisTemplate");
            }
        }
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }

    public RedisTemplate initRedisTemplate(Environment env, String redisPrefix) {
        Binder binder = Binder.get(env);
        Map config = binder.bind("spring.redis", Bindable.of(Map.class)).get();
        int maxIdle = Integer.valueOf((String) config.get("maxIdle"));
        int minIdle = Integer.valueOf((String) config.get("minIdle"));
        int maxTotal = Integer.valueOf((String) config.get("maxTotal"));
        int index = 0;
        int maxWaitMillis = Integer.valueOf((String) config.get("maxWaitMillis"));
        boolean testOnBorrow = Boolean.valueOf((String) config.get("testOnBorrow"));
        Map hostMap = binder.bind(redisPrefix, Bindable.of(Map.class)).get();
        String host = hostMap.get("host").toString();
        int port = Integer.parseInt(hostMap.get("port").toString());

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory(host, port, maxIdle, minIdle, maxTotal, index, maxWaitMillis, testOnBorrow));

        RedisSerializer defaultSerializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializer keyRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(keyRedisSerializer);
        redisTemplate.setValueSerializer(defaultSerializer);
        redisTemplate.setHashKeySerializer(keyRedisSerializer);
        redisTemplate.setHashValueSerializer(defaultSerializer);
        redisTemplate.setDefaultSerializer(defaultSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private LettuceConnectionFactory connectionFactory(String hostName, int port, int maxIdle, int minIdle,
                                                       int maxTotal, int index, long maxWaitMillis, boolean testOnBorrow) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, port);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
        // https://blog.csdn.net/huxinyao9/article/details/90767141
        factory.afterPropertiesSet();
        return factory;
    }

}
