package com.funshion.activity.common.ds;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Random;

@Aspect
@Component
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("execution(* com.funshion.activity.*.mapper.*.*(..))")
    public void changeDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.setDataSourceRouterKey(DataSourceType.MASTER);
        Signature signature = point.getSignature();
        Class clazz = signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        DataSource ds = null;
        if (clazz.isAnnotationPresent(DataSource.class)) {
            ds = (DataSource) clazz.getAnnotation(DataSource.class);
        }
        if (method.isAnnotationPresent(DataSource.class)) {
            ds = method.getAnnotation(DataSource.class);
        }
        String dsId;
        if (ds == null) {
            dsId = DataSourceType.MASTER;
        } else {
            dsId = ds.value();
        }

        if (DataSourceType.SLAVE.equals(dsId)) {
            Random rdm = new Random(System.currentTimeMillis());
            if (Math.abs(rdm.nextInt()) % 2 == 1) {
                dsId = DataSourceType.SLAVE_01;
            } else {
                dsId = DataSourceType.SLAVE_02;
            }
        }
        if (DynamicDataSourceContextHolder.dataSourceIds.contains(dsId)) {
            DynamicDataSourceContextHolder.setDataSourceRouterKey(dsId);
            logger.debug("Use DataSource :{} >", dsId, point.getSignature());
        } else {
            logger.info("数据源[{}]不存在，使用默认数据源 >{}", dsId, point.getSignature());
        }
    }

//    //@Before("@annotation(ds)")
//    public void changeDataSource(JoinPoint point, DataSource ds) throws Throwable {
//        DynamicDataSourceContextHolder.setDataSourceRouterKey(dsId);
//        // 默认数据源
//        String dsId = "";
//        if (ds == null) {
//            dsId = DataSourceType.MASTER;
//        } else {
//            dsId = ds.value();
//        }
//
//        // 从库负载均衡
//        if (DataSourceType.SLAVE.equals(dsId)) {
//            Random rdm = new Random(System.currentTimeMillis());
//            if (Math.abs(rdm.nextInt()) % 2 == 1) {
//                dsId = DataSourceType.SLAVE_01;
//            } else {
//                dsId = DataSourceType.SLAVE_02;
//            }
//        }
//        if (DynamicDataSourceContextHolder.dataSourceIds.contains(dsId)) {
//            DynamicDataSourceContextHolder.setDataSourceRouterKey(dsId);
//            logger.debug("Use DataSource :{} >", dsId, point.getSignature());
//        } else {
//            logger.info("数据源[{}]不存在，使用默认数据源 >{}", dsId, point.getSignature());
//        }
//    }

    //去掉after是因为要去重新获取注解，影响性能，直接在before上set即可
//    @After("@annotation(ds)")
//    public void restoreDataSource(JoinPoint point, DataSource ds) {
//        logger.debug("Revert DataSource : " + ds.value() + " > " + point.getSignature());
//        DynamicDataSourceContextHolder.removeDataSourceRouterKey();
//
//    }
}