//package com.funshion.activity.configuration;
//
//import com.funshion.activity.common.redis.RedisInit;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
//import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
//import org.springframework.beans.factory.config.BeanDefinitionHolder;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.*;
//import org.springframework.boot.context.properties.bind.Bindable;
//import org.springframework.boot.context.properties.bind.Binder;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.AnnotationBeanNameGenerator;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.data.redis.core.RedisTemplate;
//
//@Configuration
//public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {
//
//
//    private DefaultListableBeanFactory defaultListableBeanFactory;
//    // bean的名称生成器
//    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
//
//    private Environment env;
//
//    /**
//     * 先执行postProcessBeanDefinitionRegistry方法
//     * <p>
//     * 在执行postProcessBeanFactory方法
//     */
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {
//    }
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        redisRegistry(registry);
//    }
//
//    public void redisRegistry(BeanDefinitionRegistry registry) {
//        RedisInit redisInit = new RedisInit();
//        String redisNames = Binder.get(env).bind("spring.redis.names", Bindable.of(String.class)).get();
//        for (String name : StringUtils.split(redisNames, ",")) {
//            RedisTemplate redisTemplate = redisInit.initRedisTemplate(env, "spring.redis." + name);
//            String beanName = "redisTemplate" + name;
//            defaultListableBeanFactory.registerSingleton(beanName, redisTemplate);
//            if (!defaultListableBeanFactory.containsBean("redisTemplate")) {
//                defaultListableBeanFactory.registerAlias(beanName, "redisTemplate");
//            }
//        }
//    }
//
//
//    /**
//     * 公共注册的方法
//     *
//     * @param registry
//     * @param name
//     * @param beanClass
//     */
//
//    private void registerBean(BeanDefinitionRegistry registry, String name, Class beanClass) {
//
//        AnnotatedBeanDefinition annotatedBeanDefinition = new AnnotatedGenericBeanDefinition(beanClass);
//
//        // 可以自动生成name
//        String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(annotatedBeanDefinition, registry));
//        // bean注册的holer类.
//        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(annotatedBeanDefinition, beanName);
//        // 使用bean注册工具类进行注册.
//        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
//
//    }
//
//    @Override
//    public void setEnvironment(Environment env) {
//        // TODO Auto-generated method stub
//        this.env = env;
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext)
//            throws BeansException {
//        // TODO Auto-generated method stub
//        this.defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
//    }
//
//}